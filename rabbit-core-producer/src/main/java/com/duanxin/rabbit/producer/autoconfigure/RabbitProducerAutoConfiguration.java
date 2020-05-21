package com.duanxin.rabbit.producer.autoconfigure;

import com.duanxin.rabbit.producer.broker.ProducerClient;
import com.duanxin.rabbit.producer.broker.RabbitBroker;
import com.duanxin.rabbit.producer.broker.RabbitBrokerImpl;
import com.duanxin.rabbit.producer.broker.RabbitTemplateContainer;
import com.duanxin.rabbit.producer.mapper.BrokerMessageMapper;
import com.duanxin.rabbit.producer.service.MessageStoreService;
import com.duanxin.rabbit.producer.task.RetryMessageDataflowJob;
import com.duanxin.rabbit.task.annotation.EnableElasticJob;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 生产者自动装配
 * @author duanxin
 * @version 1.0
 * @date 2020/4/20 16:36
 */
@Configuration
@ComponentScan(basePackages = {"com.duanxin.rabbit.producer.*"})
@EnableElasticJob
public class RabbitProducerAutoConfiguration {

    @Bean
    public RabbitTemplateContainer rabbitTemplateContainer(ConnectionFactory connectionFactory,
                                                           MessageStoreService messageStoreService) {
        return new RabbitTemplateContainer(connectionFactory, messageStoreService);
    }

    @Bean
    public RabbitBroker rabbitBroker(RabbitTemplateContainer rabbitTemplateContainer,
                                     MessageStoreService messageStoreService) {
        return new RabbitBrokerImpl(rabbitTemplateContainer, messageStoreService);
    }

    @Bean
    public ProducerClient producerClient(RabbitBroker rabbitBroker) {
        return new ProducerClient(rabbitBroker);
    }

    @Bean
    public MessageStoreService messageStoreService(BrokerMessageMapper brokerMessageMapper) {
        return new MessageStoreService(brokerMessageMapper);
    }

    @Bean
    public RetryMessageDataflowJob retryMessageDataflowJob(MessageStoreService messageStoreService,
                                                           RabbitBroker rabbitBroker) {
        return new RetryMessageDataflowJob(messageStoreService, rabbitBroker);
    }

}
