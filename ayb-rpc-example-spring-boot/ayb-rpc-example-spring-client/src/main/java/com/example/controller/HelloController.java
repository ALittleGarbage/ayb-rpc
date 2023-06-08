package com.example.controller;

import com.ayb.service.HelloService;
import com.ayb.spring.boot.starter.annotation.RpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试rpc
 *
 * @author ayb
 * @date 2023/6/7
 */
@RestController
public class HelloController {

    @RpcReference
    private HelloService helloService;

    @GetMapping("/sayHello")
    String say(@RequestParam String name) {
        return helloService.sayHello(name);
    }
}
