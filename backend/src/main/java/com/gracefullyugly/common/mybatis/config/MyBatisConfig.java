package com.gracefullyugly.common.mybatis.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class MyBatisConfig {
    @Bean(value = "mybatisDataSource", destroyMethod = "close")
    public DataSource mybatisDataSource(Environment environment) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(environment.getProperty("spring.datasource.hikari.driver-class-name"));
        hikariConfig.setJdbcUrl(environment.getProperty("spring.datasource.hikari.jdbc-url"));
        hikariConfig.setUsername(environment.getProperty("spring.datasource.hikari.username"));
        hikariConfig.setPassword(environment.getProperty("spring.datasource.hikari.password"));
        hikariConfig.setPoolName(environment.getProperty("spring.datasource.hikari.pool-name"));
        hikariConfig.setMinimumIdle(Integer.parseInt(environment.getProperty("spring.datasource.hikari.minimum-idle")));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("spring.datasource.hikari.maximum-pool-size")));
        hikariConfig.setConnectionTimeout(Long.parseLong(environment.getProperty("spring.datasource.hikari.connectionTimeout")));
        hikariConfig.setValidationTimeout(Long.parseLong(environment.getProperty("spring.datasource.hikari.validationTimeout")));

        return new HikariDataSource(hikariConfig);
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(DataSource mybatisDataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        sqlSessionFactoryBean.setDataSource(mybatisDataSource);
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:mappers/**/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(destroyMethod = "clearCache")
    public SqlSessionTemplate getSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        transactionManager.setGlobalRollbackOnParticipationFailure(false);

        return transactionManager;
    }
}
