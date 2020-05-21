package com.duanxin.rabbit.producer.broker;

import com.duanxin.rabbit.api.*;
import com.duanxin.rabbit.api.exception.MessageRuntimeException;
import com.duanxin.rabbit.common.convert.GenericMessageConverter;
import com.duanxin.rabbit.common.convert.RabbitMessageConverter;
import com.duanxin.rabbit.common.serializer.impl.JacksonSerializerFactory;
import com.duanxin.rabbit.producer.service.MessageStoreService;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 池化RabbitTemplate封装
 * 每个Topic对应一个RabbitTemplate
 * 1、提高发送效率
 * 2、可以根据不同的需求定制化不同的RabbitTemplate
 * @author duanxin
 * @version 1.0
 * @date 2020/4/21 9:36
 */
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

    private static final Logger log = LoggerFactory.getLogger(RabbitTemplateContainer.class.getName());

    /**
     * String: rabbitmq topic
     * RabbitTemplate
     */
    private Map<String, RabbitTemplate> rabbitMap = Maps.newConcurrentMap();

    private ConnectionFactory connectionFactory;

    private MessageStoreService messageStoreService;

    public RabbitTemplateContainer(ConnectionFactory connectionFactory, MessageStoreService messageStoreService) {
        this.connectionFactory = connectionFactory;
        this.messageStoreService = messageStoreService;
    }

    private Splitter splitter = Splitter.on("#");

    private JacksonSerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    /**
     * 获取RabbitTemplate
     * @param message 消息体
     * @date 2020/4/21 9:42
     * @return org.springframework.amqp.rabbit.core.RabbitTemplate
     **/
    public RabbitTemplate getTemplate(Message message) throws MessageRuntimeException {
        Preconditions.checkNotNull(message);
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
        if (rabbitTemplate != null) {
            return rabbitTemplate;
        }
        log.info("#RabbitTemplateContainer.getTemplate# topic:{} is not exist, create one", topic);
        RabbitTemplate newRabbitTemplate = new RabbitTemplate(connectionFactory);
        newRabbitTemplate.setExchange(topic);
        newRabbitTemplate.setRoutingKey(message.getRoutingKey());
        newRabbitTemplate.setRetryTemplate(new RetryTemplate());
        // 对message的序列化方式
        GenericMessageConverter gmc = new GenericMessageConverter(serializerFactory.create());
        RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
        newRabbitTemplate.setMessageConverter(rmc);

        String messageType = message.getMessageType();
        if (!MessageType.RAPID.equals(messageType)) { // 进行confirm确认
            newRabbitTemplate.setConfirmCallback(this);
        }

        rabbitMap.putIfAbsent(topic, newRabbitTemplate);
        return rabbitMap.get(topic);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 具体的消息应答
        List<String> strings = splitter.splitToList(Objects.requireNonNull(correlationData.getId()));
        String messageId = strings.get(0);
        long sendTime = Long.parseLong(strings.get(1));
        String messageType = strings.get(2);
        if (ack) {

            // 当 Broker 返回ACK成功时，更新日志表中消息发送状态为SEND_OK
            // 当消息类型为 reliant ，则进行数据库查找并更新状态
            if (MessageType.RELIANT.endsWith(messageType)) {
                this.messageStoreService.success(messageId);
            }
            log.info("send message is OK, messageId:{}, send time:{}", messageId, sendTime);
        } else {
            log.error("send message is Fail, messageId:{}, send time:{}", messageId, sendTime);
        }
    }
}
