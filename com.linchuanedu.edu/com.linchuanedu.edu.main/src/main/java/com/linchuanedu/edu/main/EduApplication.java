package com.linchuanedu.edu.main;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = {"com.linchuanedu.edu"})
@MapperScan("com.linchuanedu.edu.dao")
public class EduApplication {

    private static final Logger logger = LoggerFactory.getLogger(EduApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);

        logger.info("服务器启动成功..............");

    }
}
