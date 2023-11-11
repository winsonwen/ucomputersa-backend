package com.ucomputersa.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UComputerSAGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(UComputerSAGatewayApplication.class, args);
    }

}
