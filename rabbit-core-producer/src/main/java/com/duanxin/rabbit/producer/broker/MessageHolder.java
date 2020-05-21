package com.duanxin.rabbit.producer.broker;

import com.duanxin.rabbit.api.Message;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author duanxin
 * @version 1.0
 * @className MessageHolder
 * @date 2020/05/21 14:38
 */
public class MessageHolder {

    private List<Message> messages = Lists.newArrayList();

    @SuppressWarnings({"rawtypes, unchecked"})
    public static final ThreadLocal<MessageHolder> holder = new ThreadLocal(){
        @Override
        protected Object initialValue() {
            return new MessageHolder();
        }
    };

    /**
     * 存消息
     * @param message 消息实体
     * @date 2020/5/21 14:43
     * @return void
     */
    public static void add(Message message) {
        holder.get().messages.add(message);
    }

    /**
     * 清除消息缓存
     * @date 2020/5/21 14:45
     * @return java.util.List<com.duanxin.rabbit.api.Message>
     */
    public static List<Message> clear() {
        List<Message> tmp = Lists.newArrayList(holder.get().messages);
        holder.remove();
        return tmp;
    }
}
