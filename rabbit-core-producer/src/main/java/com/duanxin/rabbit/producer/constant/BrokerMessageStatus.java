package com.duanxin.rabbit.producer.constant;

/**
 * 消息发送状态枚举类
 * @author duanxin
 * @version 1.0
 * @className BrokerMessageStatus
 * @date 2020/04/24 15:06
 */
public enum BrokerMessageStatus {

    SENDING("0"),
    SEND_OK("1"),
    SEND_FAIL("2"),
    SEND_FAIL_A_MOMENT("3");

    private String code;

    private BrokerMessageStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
