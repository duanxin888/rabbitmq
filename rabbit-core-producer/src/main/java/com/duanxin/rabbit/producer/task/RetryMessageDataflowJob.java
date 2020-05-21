package com.duanxin.rabbit.producer.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.duanxin.rabbit.producer.broker.RabbitBroker;
import com.duanxin.rabbit.producer.constant.BrokerMessageStatus;
import com.duanxin.rabbit.producer.entity.BrokerMessage;
import com.duanxin.rabbit.producer.service.MessageStoreService;
import com.duanxin.rabbit.task.annotation.ElasticJobConfig;
import com.sun.corba.se.pept.broker.Broker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务：消息重投递
 * @author duanxin
 * @version 1.0
 * @className RetryMessageDataflowJob
 * @date 2020/05/20 14:40
 */
@ElasticJobConfig(
        jobName = "com.duanxin.rabbit.producer.task.RetryMessageDataflowJob",
        cron = "0/10 * * * * ?", // 每10秒执行一次
        description = "消息补偿定时任务",
        overwrite = true, // 覆盖本地配置
        shardingTotalCount = 1
)
public class RetryMessageDataflowJob implements DataflowJob<BrokerMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryMessageDataflowJob.class);

    private MessageStoreService messageStoreService;

    private RabbitBroker rabbitBroker;

    public RetryMessageDataflowJob(MessageStoreService messageStoreService, RabbitBroker rabbitBroker) {
        this.messageStoreService = messageStoreService;
        this.rabbitBroker = rabbitBroker;
    }

    /** 消息最大重试次数 */
    private static final int MAX_RETRY_COUNT = 3;

    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        List<BrokerMessage> list = messageStoreService.fetchTimeoutMessage4Retry(BrokerMessageStatus.SENDING);
        LOGGER.info(">>>>>>>>>>>>#RetryMessageDataflowJob.fetchData# 执行，抓取数据，数量：{}<<<<<<<<<<<<<<<", list.size());
        return list;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<BrokerMessage> data) {
        data.forEach(brokerMessage -> {
            String messageId = brokerMessage.getMessageId();
            if (brokerMessage.getTryCount() >= MAX_RETRY_COUNT) {
                messageStoreService.failure(messageId);
                LOGGER.warn(">>>>>>>>>>>#RetryMessageDataflowJob.processData# 执行，消息设置为最终失败，消息ID: {}<<<<<<<<<<<<<<", messageId);
            } else {
                // 更新重试次数
                messageStoreService.update4TryCount(messageId);
                rabbitBroker.reliantSend(brokerMessage.getMessage());
                LOGGER.info(">>>>>>>>>>#RetryMessageDataflowJob.processData# 执行，消息ID：{}, 重试次数：{}<<<<<<<<<<<",
                        messageId,
                        brokerMessage.getTryCount() + 1);
            }
        });
    }
}
