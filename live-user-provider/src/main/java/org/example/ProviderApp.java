package org.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class ProviderApp {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ProviderApp.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }
}