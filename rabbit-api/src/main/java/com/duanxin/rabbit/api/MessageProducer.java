package com.duanxin.rabbit.api;

import com.duanxin.rabbit.api.exception.MessageRuntimeException;
import java.util.List;

/**
 * 消息生产者接口
 * @author duanxin
 * @version 1.0
 * @date 2020/4/20 16:16
 */
public interface MessageProducer {

    /**
     * 消息的发送，附带SendCallback回调执行响应的业务逻辑处理
     * @param message 消息体
     * @param sendCallback 消息发送回调
     * @date 2020/4/20 16:22
     **/
    void send(Message message, SendCallback sendCallback) throws MessageRuntimeException;

    /**
     * 发送消息
     * @param message 消息体
     * @date 2020/4/20 16:18
     * @throws MessageRuntimeException
     **/
    void send(Message message) throws MessageRuntimeException;

    /**
     * 发送批量消息
     * @param messages 批量消息体
     * @date 2020/4/20 16:19
     * @throws MessageRuntimeException
     **/
    void send(List<Message> messages) throws MessageRuntimeException;
}
