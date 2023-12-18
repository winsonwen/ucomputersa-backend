package ucomputersa;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class UcomputersaOrderApplication {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}