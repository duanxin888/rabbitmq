package com.duanxin.rabbit.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 消息实体封装类
 * @author duanxin
 * @version 1.0
 * @date 2020/4/20 15:31
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 4894066277631076841L;

    /** 消息唯一ID */
    private String messageId;

    /** 消息主题 */
    private String topic;

    /** 消息的路由规则 */
    private String routingKey = "";

    /** 消息的附加属性 */
    private Map<String, Object> attributes = new HashMap<>();

    /** 延迟消息的参数配置 */
    private int delayMillis;

    /** 消息类型:默认为确认消息模式 */
    private String messageType = MessageType.CONFIRM;

    public Message() {
    }

    public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMillis) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMillis = delayMillis;
    }

    public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMillis, String messageType) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMillis = delayMillis;
        this.messageType = messageType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", topic='" + topic + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", attributes=" + attributes +
                ", delayMillis=" + delayMillis +
                ", messageType='" + messageType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Message message = (Message) o;

        if (delayMillis != message.delayMillis) {
            return false;
        }
        if (!Objects.equals(messageId, message.messageId)) {
            return false;
        }
        if (!Objects.equals(topic, message.topic)) {
            return false;
        }
        if (!Objects.equals(routingKey, message.routingKey)) {
            return false;
        }
        if (!Objects.equals(attributes, message.attributes)) {
            return false;
        }
        return Objects.equals(messageType, message.messageType);
    }

    @Override
    public int hashCode() {
        int result = messageId != null ? messageId.hashCode() : 0;
        result = 31 * result + (topic != null ? topic.hashCode() : 0);
        result = 31 * result + (routingKey != null ? routingKey.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + delayMillis;
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        return result;
    }
}
