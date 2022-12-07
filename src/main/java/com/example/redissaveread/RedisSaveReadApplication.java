package com.example.redissaveread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RedisSaveReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisSaveReadApplication.class, args);
    }

}
