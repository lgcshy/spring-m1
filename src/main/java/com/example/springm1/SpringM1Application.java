package com.example.springm1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springm1.mapper")
public class SpringM1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringM1Application.class, args);
    }

} 