package com.duanxin.rabbit.producer.broker;

import com.duanxin.rabbit.api.*;
import com.duanxin.rabbit.producer.constant.BrokerMessageConst;
import com.duanxin.rabbit.producer.constant.BrokerMessageStatus;
import com.duanxin.rabbit.producer.entity.BrokerMessage;
import com.duanxin.rabbit.producer.service.MessageStoreService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 具体发送不同种类消息的实现类
 * @author duanxin
 * @version 1.0
 * @date 2020/4/21 9:03
 */
public class RabbitBrokerImpl implements RabbitBroker {

    private static final Logger log = LoggerFactory.getLogger(RabbitBrokerImpl.class.getName());

    private RabbitTemplateContainer rabbitTemplateContainer;

    private MessageStoreService messageStoreService;

    public RabbitBrokerImpl(RabbitTemplateContainer rabbitTemplateContainer, MessageStoreService messageStoreService) {
        this.rabbitTemplateContainer = rabbitTemplateContainer;
        this.messageStoreService = messageStoreService;
    }

    /**
     * 使用异步线程池发送迅速消息
     * @param message 消息体
     * @date 2020/4/21 9:28
     **/
    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    /**
     * 发送消息的核心方法
     * @param message 消息体
     * @date 2020/4/21 9:05
     **/
    private void sendKernel(Message message) {
        // 进行异步消息发送
        AsyncBaseQueue.submit((Runnable)() -> {
            CorrelationData correlationData =
                    new CorrelationData(String.format("%s#%s#%s",
                            message.getMessageId(),
                            System.currentTimeMillis(),
                            message.getMessageType()));
            String routingKey = message.getRoutingKey();
            String topic = message.getTopic();
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq, messageId:{}",
                    message.getMessageId());
        });
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {
        message.setMessageType(MessageType.RELIANT);
        BrokerMessage bm = messageStoreService.selectByMessageId(message.getMessageId());
        if (bm == null) { // 判断消息是否是第一次投递
            // 1. 把数据库的消息发送日志先记录好
            Date now = new Date();
            BrokerMessage brokerMessage = new BrokerMessage();
            brokerMessage.setMessageId(message.getMessageId());
            brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
            // tryCount 在最开始发送时不需要进行设置
            brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
            brokerMessage.setCreateTime(now);
            brokerMessage.setUpdateTime(now);
            brokerMessage.setMessage(message);
            // 消息持久化
            this.messageStoreService.insert(brokerMessage);
        }

        // 2。 执行真正的发送消息逻辑
        sendKernel(message);
    }

    @Override
    public void sendMessages() {
        // 把消息从线程池中取出来
        List<Message> messages = MessageHolder.clear();
        messages.forEach(message -> {
            // 进行异步消息发送
            MessageHolderAsyncQueue.submit((Runnable)() -> {
                CorrelationData correlationData =
                        new CorrelationData(String.format("%s#%s#%s",
                                message.getMessageId(),
                                System.currentTimeMillis(),
                                message.getMessageType()));
                String routingKey = message.getRoutingKey();
                String topic = message.getTopic();
                RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
                rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
                log.info("#RabbitBrokerImpl.sendMessages# send to rabbitmq, messageId:{}",
                        message.getMessageId());
            });
        });
    }
}
