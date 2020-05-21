package com.duanxin.rabbit.api.exception;

/**
 * 基本消息异常类
 * @author duanxin
 * @version 1.0
 * @date 2020/4/20 16:09
 */
public class MessageException extends Exception {
    private static final long serialVersionUID = -3132234077318094408L;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }
}
