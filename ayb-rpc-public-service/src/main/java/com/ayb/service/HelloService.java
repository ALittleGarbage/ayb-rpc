package com.ayb.service;

/**
 * 公共rpc接口
 *
 * @author ayb
 * @date 2023/6/5
 */
public interface HelloService {

    /**
     * 你好
     *
     * @param name 名字
     * @return 你好!
     */
    String sayHello(String name);
}
