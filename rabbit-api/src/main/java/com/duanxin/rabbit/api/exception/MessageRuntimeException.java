package com.duanxin.rabbit.api.exception;

/**
 * 运行时消息异常类
 * @author duanxin
 * @version 1.0
 * @date 2020/4/20 16:09
 */
public class MessageRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -3132234077318094408L;

    public MessageRuntimeException() {
        super();
    }

    public MessageRuntimeException(String message) {
        super(message);
    }

    public MessageRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageRuntimeException(Throwable cause) {
        super(cause);
    }
}
