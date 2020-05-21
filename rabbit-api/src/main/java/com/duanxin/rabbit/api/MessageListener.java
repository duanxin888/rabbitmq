package com.duanxin.rabbit.api;

/**
 * 消费者消息监听接口
 * @author duanxin
 * @version 1.0
 * @date 2020/4/20 16:17
 */
public interface MessageListener {

    /**
     *
     * @param message 消息体
     * @date 2020/4/20 16:24
     **/
    void onMessage(Message message);
}
