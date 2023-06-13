package com.yejh.jcode.sboot.restful.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PgDSConfig {

    @Bean("pgDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.postgresql")
    public DataSourceProperties getDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("pgDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.postgresql.configuration")
    public HikariDataSource getDataSource() {
        return getDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

}