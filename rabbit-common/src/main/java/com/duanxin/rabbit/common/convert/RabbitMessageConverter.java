package com.duanxin.rabbit.common.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * Rabbit消息转换器
 * @author duanxin
 * @version 1.0
 * @date 2020/4/22 10:26
 */
public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter genericMessageConverter;

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.genericMessageConverter = genericMessageConverter;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        com.duanxin.rabbit.api.Message message = (com.duanxin.rabbit.api.Message) object;
        messageProperties.setDelay(message.getDelayMillis());
        return this.genericMessageConverter.toMessage(object, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return (com.duanxin.rabbit.api.Message) this.genericMessageConverter.fromMessage(message);
    }
}
