package com.duanxin.rabbit.producer.entity;

import com.duanxin.rabbit.api.Message;

import java.util.Date;
import java.io.Serializable;

/**
 * (BrokerMessage)实体类
 *
 * @author makejava
 * @since 2020-04-24 11:01:46
 */
public class BrokerMessage implements Serializable {
    private static final long serialVersionUID = 557590153466660244L;
    
    private String messageId;

    private Message message;
    
    private Integer tryCount = 0;
    
    private String status;
    
    private Date nextRetry;
    
    private Date createTime;
    
    private Date updateTime;


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getNextRetry() {
        return nextRetry;
    }

    public void setNextRetry(Date nextRetry) {
        this.nextRetry = nextRetry;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}