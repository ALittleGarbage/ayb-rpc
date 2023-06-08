package com.ayb.example.impl;

import com.ayb.service.HelloService;

/**
 * HelloService实现类
 *
 * @author ayb
 * @date 2023/6/5
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "你好!" + name;
    }
}
