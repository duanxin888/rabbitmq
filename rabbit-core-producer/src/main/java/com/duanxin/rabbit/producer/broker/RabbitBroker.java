package com.duanxin.rabbit.producer.broker;

import com.duanxin.rabbit.api.Message;

/**
 * 具体发送不同种类消息的接口
 * @author duanxin
 * @version 1.0
 * @date 2020/4/21 8:58
 */
public interface RabbitBroker {

    /**
     * 发送迅速消息
     * @param message 消息体
     * @date 2020/4/21 9:00
     **/
    void rapidSend(Message message);

    /**
     * 发送确认消息
     * @param message 消息体
     * @date 2020/4/21 9:00
     **/
    void confirmSend(Message message);

    /**
     * 发送可靠性消息
     * @param message 消息体
     * @date 2020/4/21 9:00
     **/
    void reliantSend(Message message);

    /**
     * 发送批量消息
     * @date 2020/4/21 9:01
     **/
    void sendMessages();
}
