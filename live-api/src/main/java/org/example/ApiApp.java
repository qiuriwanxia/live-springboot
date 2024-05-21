package org.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.example.controller.ApiTestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class ApiApp {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication();
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);
        ConfigurableApplicationContext run = springApplication.run(ApiApp.class);

    }

}
