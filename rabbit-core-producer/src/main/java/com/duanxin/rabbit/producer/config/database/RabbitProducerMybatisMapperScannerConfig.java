package com.duanxin.rabbit.producer.config.database;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mapper扫描配置
 * @author duanxin
 * @version 1.0
 * @className RabbitProducerMybatisMapperScannerConfig
 * @date 2020/04/24 11:11
 */
@Configuration
@AutoConfigureAfter({RabbitProducerDataSourceConfiguration.class})
public class RabbitProducerMybatisMapperScannerConfig {

    @Bean(name = "rabbitProducerMapperScannerConfigurer")
    public MapperScannerConfigurer rabbitProducerMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("rabbitProducerSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.duanxin.rabbit.producer.mapper");
        return mapperScannerConfigurer;
    }
}
