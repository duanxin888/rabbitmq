package com.duanxin.rabbit.api;

/**
 * 消息发送回调接口
 * @author duanxin
 * @version 1.0
 * @date 2020/4/20 16:20
 */
public interface SendCallback {

    /**
     * 消息发送成功
     * @date 2020/4/20 16:20
     **/
    void onSuccess();

    /**
     * 消息发送失败
     * @date 2020/4/20 16:20
     **/
    void onFailure();
}
