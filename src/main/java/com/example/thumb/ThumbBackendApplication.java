package com.example.thumb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.thumb.**.mapper")
public class ThumbBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThumbBackendApplication.class, args);
    }

}
