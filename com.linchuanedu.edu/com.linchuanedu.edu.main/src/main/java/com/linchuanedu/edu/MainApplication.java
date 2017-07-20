package com.linchuanedu.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);

        System.out.println("服务器启动..............");

        for (int i = 0; i < 10; i++){
            System.out.println(i);
        }
    }
}
