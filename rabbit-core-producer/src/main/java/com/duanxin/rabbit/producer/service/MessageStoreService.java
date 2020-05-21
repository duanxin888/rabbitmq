package com.duanxin.rabbit.producer.service;

import com.duanxin.rabbit.producer.constant.BrokerMessageStatus;
import com.duanxin.rabbit.producer.entity.BrokerMessage;
import com.duanxin.rabbit.producer.mapper.BrokerMessageMapper;
import com.sun.corba.se.pept.broker.Broker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 消息持久化服务
 * @author duanxin
 * @version 1.0
 * @className MessageStoreService
 * @date 2020/04/24 15:03
 */
public class MessageStoreService {

    private BrokerMessageMapper brokerMessageMapper;

    public MessageStoreService(BrokerMessageMapper brokerMessageMapper) {
        this.brokerMessageMapper = brokerMessageMapper;
    }

    public int insert(BrokerMessage brokerMessage) {
        return this.brokerMessageMapper.insert(brokerMessage);
    }

    public BrokerMessage selectByMessageId(String messageId) {
        return brokerMessageMapper.queryById(messageId);
    }

    /**
     * 更新消息的重试次数
     * @param brokerMessageId 消息ID
     * @date 2020/5/20 15:34
     * @return void
     */
    public int update4TryCount(String brokerMessageId) {
        return brokerMessageMapper.update4TryCount(brokerMessageId, new Date());
    }

    /**
     * 查询超时消息
     * @param brokerMessageStatus 消息状态
     * @date 2020/5/20 15:00
     * @return java.util.List<com.duanxin.rabbit.producer.entity.BrokerMessage>
     */
    public List<BrokerMessage> fetchTimeoutMessage4Retry(BrokerMessageStatus brokerMessageStatus) {
        return brokerMessageMapper.selectBrokerMessageStatus4Timeout(brokerMessageStatus.getCode());
    }

    /**
     * 发送成功接收到Broker返回ACK，更改日志表中消息的状态为 SEND_OK
     * @param messageId 消息主键id
     * @date 2020/4/24 15:24
     * @return void
     */
    public void success(String messageId) {
        this.brokerMessageMapper.changeBrokerMessageStatus(messageId,
                BrokerMessageStatus.SEND_OK.getCode(),
                new Date());
    }

    /**
     * 更改日志表中消息状态为 SEND_FAIL
     * @param messageId 消息主键id
     * @date 2020/4/24 15:32
     * @return void
     */
    public void failure(String messageId) {
        this.brokerMessageMapper.changeBrokerMessageStatus(messageId,
                BrokerMessageStatus.SEND_FAIL.getCode(),
                new Date());
    }
}
