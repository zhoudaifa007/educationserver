package com.linchuanedu.edu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EduApplication {

    private static final Logger logger = LoggerFactory.getLogger(EduApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);

        logger.info("服务器启动成功..............");

    }
}
