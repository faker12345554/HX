package com.hx.hx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HxApplication {

    public static void main(String[] args) {
        SpringApplication.run(HxApplication.class, args);
    }

}
