package org.example.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ShardJdbcDatasourceAutoinitConfig {


    @Bean
    public ApplicationRunner runner(DataSource dataSource) {
        return args -> {
            System.out.println("dataSource.getConnection() = " + dataSource.getConnection());
        };
    }

}
