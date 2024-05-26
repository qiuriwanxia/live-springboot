package org.example.key;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisKeyBuilder {


    @Value("${spring.application.name}")
    private String applicationName;

    public static   final String SPLIT = ":";

    public String getPrefix() {
        return applicationName +SPLIT;
    }

    public String getSplit(){
        return SPLIT;
    }

}
