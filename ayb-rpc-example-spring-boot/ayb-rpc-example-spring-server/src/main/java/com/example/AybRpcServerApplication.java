package com.example;

import com.ayb.spring.boot.starter.annotation.EnableAybRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ayb
 * @date 2023/6/7
 */
@SpringBootApplication
@EnableAybRpc
public class AybRpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AybRpcServerApplication.class, args);
    }

}
