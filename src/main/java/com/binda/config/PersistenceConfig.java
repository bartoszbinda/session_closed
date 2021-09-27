/*
 * Copyright (c) 2020, Sabre Holdings. All Rights Reserved.
 */

package com.binda.config;

import java.util.Properties;;
import javax.sql.DataSource;

import com.binda.domain.TimeZone;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class PersistenceConfig {

    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.binda.domain");
        sessionFactory.setHibernateProperties(new Properties() {
            {
                setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
            }
        });
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(env.getProperty("databaseUrl"));
        ds.setUsername(env.getProperty("user"));
        ds.setPassword(env.getProperty("password"));
        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        ds.setInitialSize(5);
        return ds;
    }
    @Bean
    public TimeZone timeZone(){
        return new TimeZone();
    }
}
