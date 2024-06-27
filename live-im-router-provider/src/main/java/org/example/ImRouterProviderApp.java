package org.example;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.example.dto.ImMessageBody;
import org.example.service.ImRouterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class ImRouterProviderApp implements CommandLineRunner {

    @Resource
    private ImRouterService imRouterService;

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication();
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(ImRouterProviderApp.class);

    }

    @Override
    public void run(String... args) throws Exception {
//        ImMessageBody imMessageBody = new ImMessageBody();
//        imRouterService.sendMsg(10086L,imMessageBody);
    }
}
