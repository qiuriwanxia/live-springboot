package org.example;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.example.enums.ImMessageAppEnum;
import org.example.service.ImTokenService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class LiveImProviderApp implements CommandLineRunner {

    @Resource
    private ImTokenService imTokenService;

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication();
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(LiveImProviderApp.class);

    }

    @Override
    public void run(String... args) throws Exception {
        String token = imTokenService.createToken(10086L, ImMessageAppEnum.LIVE_APP_BIZ.getCode());

        System.out.println("token = " + token);

        Long userId = imTokenService.getUserId(token);

        System.out.println("token = " + userId);
    }
}
