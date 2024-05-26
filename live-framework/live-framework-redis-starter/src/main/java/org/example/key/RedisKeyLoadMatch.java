package org.example.key;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.reflect.Field;

public class RedisKeyLoadMatch implements Condition {


    private static Logger log = LoggerFactory.getLogger(RedisKeyLoadMatch.class);

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        String applicationName = context.getEnvironment().getProperty("spring.application.name");


        if (applicationName == null || applicationName.isEmpty()) {
            log.info("未找到定义的bean");
            return false;
        }

        log.info("applicationName = " + applicationName);

        System.out.println("metadata = " + metadata);

        Field declaredField = null;
        try {
            declaredField = metadata.getClass().getDeclaredField("className");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        declaredField.setAccessible(true);
        try {
            String className = (String) declaredField.get(metadata);
            boolean contains = className.toLowerCase().contains(applicationName.replace("-", "").toLowerCase());
            return contains;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


    }


}
