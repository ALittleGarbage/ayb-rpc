package com.ayb.rpc.common.extension;

import com.ayb.rpc.common.annotation.SPI;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 扩展类加载器
 *
 * @author ayb
 * @date 2023/6/5
 */
@Slf4j
public final class ExtensionLoader<T> {

    /**
     * 扫描路径
     */
    private static final String PATH = "META-INF/aybrpc/extensions";

    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private final Class<?> type;

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("type为空");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("type必须是一个接口");
        }
        if (!type.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("type必须存在注解SPI");
        }

        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

    /**
     * 获取实例
     *
     * @param name
     * @return
     */
    public T getExtension(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name不能为空或者null");
        }
        Object instance;
        try {
            Holder<Object> holder = cachedInstances.get(name);
            if (holder == null) {
                cachedInstances.putIfAbsent(name, new Holder<>());
                holder = cachedInstances.get(name);
            }
            instance = holder.get();
            if (instance == null) {
                synchronized (holder) {
                    instance = holder.get();
                    if (instance == null) {
                        instance = createExtension(name);
                        holder.set(instance);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return (T) instance;
    }

    /**
     * 创建实例
     *
     * @param name
     * @return
     */
    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            return null;
        }
        T instance = null;
        try {
            instance = (T) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return instance;
    }

    /**
     * 获取存放在Holder中的hashMap<String, Class<?>>
     *
     * @return
     */
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = new HashMap<>(8);
                    loadDirectory(classes);
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    private void loadDirectory(Map<String, Class<?>> extensionClasses) {
        String fileName = ExtensionLoader.PATH + "/" + type.getName();
        try {
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            Enumeration<URL> urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceUrl);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 获取META-INF/extensions路径下的类
     *
     * @param extensionClasses
     * @param classLoader
     * @param resourceUrl
     */
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceUrl) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final int ci = line.indexOf(';');
                if (ci >= 0) {
                    line = line.substring(0, ci);
                }
                line = line.trim();
                if (line.length() > 0) {
                    try {
                        final int ei = line.indexOf('=');
                        String name = line.substring(0, ei).trim();
                        String clazzName = line.substring(ei + 1).trim();
                        if (name.length() > 0 && clazzName.length() > 0) {
                            Class<?> clazz = classLoader.loadClass(clazzName);
                            extensionClasses.put(name, clazz);
                        } else {
                            log.error("SPI load Class failed, name {}", name);
                        }
                    } catch (ClassNotFoundException e) {
                        log.error(e.getMessage());
                    }
                }

            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
