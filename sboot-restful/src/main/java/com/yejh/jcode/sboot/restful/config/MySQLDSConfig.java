package com.yejh.jcode.sboot.restful.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * EFOX 生产环境数据源
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2021-11-26
 * @since 1.1.0
 */
@Configuration
@MapperScan(basePackages = "com.yejh.jcode.sboot.restful.dao.mapper", sqlSessionTemplateRef = "mysqlSqlSessionTemplate")
public class MySQLDSConfig {

    @Bean("mysqlDataSourceProperties")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSourceProperties getDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("mysqlDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.mysql.configuration")
    public HikariDataSource getDataSource() {
        return getDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("mysqlSqlSessionFactory")
    @Primary
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(
                new ClassPathResource("com/yejh/jcode/sboot/restful/dao/mapper/MySQLMapper.xml")
        );
        return bean.getObject();
    }

    @Bean("mysqlTransactionManager")
    @Primary
    public DataSourceTransactionManager getTransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("mysqlSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate getSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}