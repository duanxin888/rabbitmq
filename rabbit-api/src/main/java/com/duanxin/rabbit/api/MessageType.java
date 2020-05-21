package com.duanxin.rabbit.api;

/**
 * 消息类型封装类
 * @author duanxin
 * @version 1.0
 * @date 2020/4/20 15:42
 */
public final class MessageType {

    /**
     * 迅速消息：不需要保障消息的可靠性，也不要做confirm确认
     * */
    public static final String RAPID = "0";

    /**
     * 确认消息：不需要保障消息的可靠性，但是会做消息的confirm确认
     * */
    public static final String CONFIRM = "1";

    /**
     * 可靠性消息：一定要保证消息的100%可靠性投递，不允许任何消息的丢失
     * PS：保障数据库和所发的消息是原子性的（最终一致性）
     * */
    public static final String RELIANT = "2";
}
