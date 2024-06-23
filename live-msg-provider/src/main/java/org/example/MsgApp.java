package org.example;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.example.constant.MsgSendResultEnum;
import org.example.service.TSmsService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
@MapperScan(basePackages = {"org.example.dao.mapper"})
@Slf4j
@RefreshScope
public class MsgApp implements CommandLineRunner {

    @Resource
    private TSmsService tSmsService;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MsgApp.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext run = application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        MsgSendResultEnum msgSendResultEnum = tSmsService.sendMsg("");
        System.out.println("msgSendResultEnum = " + msgSendResultEnum);
    }
}
