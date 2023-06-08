package com.ayb.core.rpc;

/**
 * 服务端接口
 *
 * @author ayb
 * @date 2023/6/5
 */
public interface Server {

    /**
     * 启动Server
     */
    void start();

    /**
     * 注册service
     *
     * @param serviceName server名称
     * @param service     service实例
     */
    void registerService(String serviceName, Object service);

    /**
     * 关闭Server
     */
    void close();
}
