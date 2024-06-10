package org.example;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.example.constant.UserTagsEnum;
import org.example.service.TUserTagService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.Time;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
@MapperScan(basePackages = {"org.example.dao.mapper"})
@Slf4j
@RefreshScope
public class UserProviderApp implements CommandLineRunner {

    @Resource
    private TUserTagService tUserTagService;

    @Value("${version}")
    private String version;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(UserProviderApp.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext run = application.run(args);

    }


    @Override
    public void run(String... args) throws Exception {

        log.info("当前程序版本 {}",version);

        tUserTagService.setTag(1L, UserTagsEnum.IS_OLD_USER);

        TimeUnit.SECONDS.sleep(1);

        boolean containTag = tUserTagService.containTag(1L, UserTagsEnum.IS_OLD_USER);
        log.info("是否是老用户 {}", containTag);
        containTag = tUserTagService.containTag(1L, UserTagsEnum.IS_VIP);
        log.info(UserTagsEnum.IS_VIP.getDesc(),containTag);

        tUserTagService.cancelTag(1L, UserTagsEnum.IS_OLD_USER);

        TimeUnit.SECONDS.sleep(1);
        containTag = tUserTagService.containTag(1L, UserTagsEnum.IS_OLD_USER);
        log.info("取消后是否是老用户 {}",containTag);
    }
}