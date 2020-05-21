package com.duanxin.rabbit.producer.config.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

/**
 * 解析执行sql文件
 * @author duanxin
 * @version 1.0
 * @className BrokerMessageConfiguration
 * @date 2020/04/24 11:10
 */
@Configuration
public class BrokerMessageConfiguration {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BrokerMessageConfiguration.class.getName());

    @javax.annotation.Resource
    private DataSource rabbitProducerDataSource;

    @Value("classpath:rabbit-producer-message-schema.sql")
    private Resource schemaScript;

    public BrokerMessageConfiguration() {
    }

    @Bean
    public DataSourceInitializer initDatabaseInitializer() {
        LOGGER.info("============ #BrokerMessageConfiguration.initDatabaseInitializer#, rabbitProducerDataSource:{}==============", rabbitProducerDataSource);
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(rabbitProducerDataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }
}
