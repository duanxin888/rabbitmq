package com.duanxin.rabbit.test;

import com.duanxin.rabbit.api.Message;
import com.duanxin.rabbit.api.MessageType;
import com.duanxin.rabbit.producer.broker.ProducerClient;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/**
 * @author duanxin
 * @version 1.0
 * @className ApplicationTest
 * @date 2020/05/20 16:01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {

    @Resource
    private ProducerClient producerClient;

    @Test
    public void testProducerClient() throws Exception{
        for (int i = 0; i < 1; ++i) {
            String uniqueId = UUID.randomUUID().toString();
            Map<String, Object> attributes = Maps.newHashMap();
            attributes.put("name", "张三");
            attributes.put("age", 18);
            Message message = new Message(
                    uniqueId,
                    "exchange-2",
                    "spring-abc",
                    attributes,
                    0
            );
            message.setMessageType(MessageType.RELIANT);
            // message.setDelayMillis(5000);
            producerClient.send(message);
        }
        Thread.sleep(1000000);
    }
}
