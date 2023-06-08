package com.ayb.core.serialize.impl;

import com.ayb.core.serialize.Serializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayOutputStream;

/**
 * Kryo序列化
 *
 * @author ayb
 * @date 2023/6/2
 */
@Slf4j
public class KryoSerializer implements Serializer {

    /**
     * Kryo线程不安全，使用ThreadLocal
     */
    private static final ThreadLocal<Kryo> KRYO = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    });

    @Override
    public byte[] serialize(Object object) {
        if (object == null) {
            return new byte[0];
        }

        Kryo kryo = KRYO.get();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (Output output = new Output(os)) {
            kryo.writeObject(output, object);
        } catch (Exception e) {
            log.error("Kryo序列化失败:{}", e.getMessage());
        }

        return os.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0 || clazz == null) {
            return null;
        }

        Kryo kryo = KRYO.get();
        try (Input input = new Input(bytes)) {
            return kryo.readObject(input, clazz);
        } catch (Exception e) {
            log.error("Kryo反序列化失败:{}", e.getMessage());
        }

        return null;
    }
}
