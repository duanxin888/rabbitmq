package com.duanxin.rabbit.producer.mapper;

import com.duanxin.rabbit.producer.entity.BrokerMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Primary;

import java.util.Date;
import java.util.List;

/**
 * (BrokerMessage)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-24 11:02:47
 */
@Mapper
public interface BrokerMessageMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param messageId 主键
     * @return 实例对象
     */
    BrokerMessage queryById(String messageId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<BrokerMessage> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param brokerMessage 实例对象
     * @return 对象列表
     */
    List<BrokerMessage> queryAll(BrokerMessage brokerMessage);

    /**
     * 新增数据
     *
     * @param brokerMessage 实例对象
     * @return 影响行数
     */
    int insert(BrokerMessage brokerMessage);

    /**
     * 修改数据
     *
     * @param brokerMessage 实例对象
     * @return 影响行数
     */
    int update(BrokerMessage brokerMessage);

    /**
     * 通过主键删除数据
     *
     * @param messageId 主键
     * @return 影响行数
     */
    int deleteById(String messageId);

    /**
     * 更新消息状态
     * @param messageId 消息主键id
     * @param code 发送成功状态
     * @param date 更新时间
     * @date 2020/4/24 15:26
     * @return void
     */
    void changeBrokerMessageStatus(@Param("messageId") String messageId,
                                   @Param("code") String code,
                                   @Param("update") Date date);

    /**
     * 查询状态为sending的超时任务
     * @param brokerMessageStatus 消息状态
     * @date 2020/5/20 14:53
     * @return java.util.List<com.duanxin.rabbit.producer.entity.BrokerMessage>
     */
    List<BrokerMessage> selectBrokerMessageStatus4Timeout(@Param("brokerMessageStatus") String brokerMessageStatus);

    /**
     * 更新消息的重试次数
     * @param brokerMessageId 消息id
     * @param updateTime 更新时间
     * @date 2020/5/20 15:30
     * @return int
     */
    int update4TryCount(@Param("brokerMessageId") String brokerMessageId, @Param("updateTime") Date updateTime);
}