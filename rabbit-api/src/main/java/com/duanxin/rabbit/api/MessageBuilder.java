package com.duanxin.rabbit.api;

import com.duanxin.rabbit.api.exception.MessageRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 消息建造对象（建造者模式）
 * @author duanxin
 * @version 1.0
 * @date 2020/4/20 15:52
 */
public class MessageBuilder {

    private String messageId;
    private String topic;
    private String routingKey = "";
    private Map<String, Object> attributes = new HashMap<>();
    private int delayMillis;
    private String messageType = MessageType.CONFIRM;

    private MessageBuilder() {

    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public MessageBuilder messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public MessageBuilder topic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder routingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public MessageBuilder attributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    public MessageBuilder delayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
        return this;
    }

    public MessageBuilder messageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public Message build() {
        if (messageId == null) {
            this.messageId = UUID.randomUUID().toString().trim();
        }

        if (topic == null) {
            throw new MessageRuntimeException("this topic is null");
        }

        Message message = new Message(messageId, topic, routingKey, attributes, delayMillis, messageType);
        return message;
    }
}
