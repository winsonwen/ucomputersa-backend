package com.ucomputersa.monolithic;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class UComputerSAMonolithicApplication {
    public static void main(String[] args) {
        SpringApplication.run(UComputerSAMonolithicApplication.class, args);
    }
}