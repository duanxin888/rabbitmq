package com.duanxin.rabbit.producer.broker;

import com.duanxin.rabbit.api.*;
import com.duanxin.rabbit.api.exception.MessageRuntimeException;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 发送消息的实际实现类
 * @author duanxin
 * @version 1.0
 * @date 2020/4/20 16:51
 */
public class ProducerClient implements MessageProducer {

    private RabbitBroker rabbitBroker;

    public ProducerClient(RabbitBroker rabbitBroker) {
        this.rabbitBroker = rabbitBroker;
    }

    @Override
    public void send(Message message) throws MessageRuntimeException {
        Preconditions.checkNotNull(message);
        String messageType = message.getMessageType();
        switch (messageType) {
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            default :
                break;
        }
    }

    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRuntimeException {

    }

    @Override
    public void send(List<Message> messages) throws MessageRuntimeException {
        messages.forEach(message -> {
            // 把消息设置为迅速消息
            message.setMessageType(MessageType.RAPID);
            MessageHolder.add(message);
        });
        // 进行发消息
        rabbitBroker.sendMessages();
    }
}
