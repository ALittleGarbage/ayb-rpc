package com.example.service;

import com.ayb.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author ayb
 * @date 2023/6/6
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "你好!" + name;
    }
}
