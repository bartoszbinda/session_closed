/*
 * Copyright (c) 2020, Sabre Holdings. All Rights Reserved.
 */

package com.binda;

import com.binda.domain.TimeZone;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

;

@Configuration
@EnableTransactionManagement
@ComponentScan
public class PersistenceConfig {

    private static final String H2_JDBC_URL_TEMPLATE = "jdbc:h2:%s/target/db/sample;AUTO_SERVER=TRUE";
    @Value("classpath:sql/seed-data.sql")
    private Resource H2_SCHEMA_SCRIPT;

    @Value("classpath:sql/test-data.sql")
    private Resource H2_DATA_SCRIPT;

    @Value("classpath:sql/drop-data.sql")
    private Resource H2_CLEANER_SCRIPT;

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
    public TimeZone timeZone() {
        return new TimeZone();
    }

    @Bean
    public DataSource dataSource(Environment env) throws Exception {
        return createH2DataSource();
    }


    @Autowired
    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        initializer.setDatabaseCleaner(databaseCleaner());
        return initializer;
    }


    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(H2_SCHEMA_SCRIPT);
        populator.addScript(H2_DATA_SCRIPT);
        return populator;
    }

    private DatabasePopulator databaseCleaner() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(H2_CLEANER_SCRIPT);
        return populator;
    }

    private DataSource createH2DataSource() {
        String jdbcUrl = String.format(H2_JDBC_URL_TEMPLATE, System.getProperty("user.dir"));
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(jdbcUrl);
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }

    @Bean
    Properties jpaProperties() {
        Properties props = new Properties();
        props.put("hibernate.query.substitutions", "true 'Y', false 'N'");
        props.put("hibernate.hbm2ddl.auto", "create-drop");
        props.put("hibernate.show_sql", "false");
        props.put("hibernate.format_sql", "true");

        return props;
    }
}
