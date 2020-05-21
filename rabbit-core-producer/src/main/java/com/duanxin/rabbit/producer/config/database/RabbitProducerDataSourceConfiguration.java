package com.duanxin.rabbit.producer.config.database;
import	java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author duanxin
 * @version 1.0
 * @className RabbitProducerDataSourceConfiguration
 * @date 2020/04/24 11:10
 */
@Configuration
@PropertySource({"classpath:rabbit-producer-message.properties"})
public class RabbitProducerDataSourceConfiguration {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(RabbitProducerDataSourceConfiguration.class.getName());

    @Value("${rabbit.producer.druid.type}")
    private Class<? extends DataSource> datasourceType;

    @Bean(name = "rabbitProducerDataSource")
    @Primary
    @ConfigurationProperties(prefix = "rabbit.producer.druid.jdbc")
    public DataSource rabbitProducerDataSource() throws SQLException{
        DataSource rabbitProducerDataSource = DataSourceBuilder.create().type(datasourceType).build();
        LOGGER.info("============== rabbitProducerDataSource:{}================", rabbitProducerDataSource);
        return rabbitProducerDataSource;
    }

    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    public DataSource primaryDataSource() {
        return primaryDataSourceProperties().initializeDataSourceBuilder().build();
    }
}
