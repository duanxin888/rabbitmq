package com.duanxin.rabbit.common.serializer.impl;

import com.duanxin.rabbit.api.Message;
import com.duanxin.rabbit.common.serializer.Serializer;
import com.duanxin.rabbit.common.serializer.SerializerFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 序列化和反序列实现
 * @author duanxin
 * @version 1.0
 * @date 2020/4/22 9:35
 */
public class JacksonSerializerFactory implements SerializerFactory {

    public static final JacksonSerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}
