package com.yejh.jcode.sboot.restful.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.yejh.jcode.sboot.restful.dao.mapper", sqlSessionTemplateRef = "ckSqlSessionTemplate")
public class CkDSConfig {

    @Bean("ckDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.clickhouse")
    public DataSourceProperties getDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("ckDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.clickhouse.configuration")
    public HikariDataSource getDataSource() {
        return getDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("ckSqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("ckDataSource") DataSource dataSource) throws Exception {
        /*
         * mybatis-plus 不要使用原生的 {@code org.mybatis.spring.SqlSessionFactoryBean}，会报错：org.apache.ibatis.binding.BindingException: Invalid bound statement (not found)
         */
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(
                new ClassPathResource("com/yejh/jcode/sboot/restful/dao/mapper/MySQLMapper.xml")
        );
        return bean.getObject();
    }

    @Bean("ckTransactionManager")
    public DataSourceTransactionManager getTransactionManager(@Qualifier("ckDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("ckSqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate(@Qualifier("ckSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}