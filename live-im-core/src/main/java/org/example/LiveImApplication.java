package org.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.example.handler.ImMessageHandlerContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class LiveImApplication {

    @Value("${netty.port}")
    public int port;

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication();
        springApplication.addInitializers();
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext run = springApplication.run(LiveImApplication.class);
        LiveImApplication bean = run.getBean(LiveImApplication.class);
        System.out.println("bean.port = " + bean.port);


    }

}
