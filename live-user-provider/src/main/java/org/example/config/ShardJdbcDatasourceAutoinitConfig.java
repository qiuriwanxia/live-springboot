package org.example.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configurable
public class ShardJdbcDatasourceAutoinitConfig {


    @Bean
    public ApplicationRunner runner(DataSource dataSource) {
        return args -> {
            System.out.println("dataSource.getConnection() = " + dataSource.getConnection());
        };
    }

}
