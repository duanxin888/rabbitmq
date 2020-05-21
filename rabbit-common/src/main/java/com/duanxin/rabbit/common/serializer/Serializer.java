package com.duanxin.rabbit.common.serializer;

/**
 * 序列化和反序列的接口
 * @author duanxin
 * @version 1.0
 * @date 2020/4/22 9:28
 */
public interface Serializer {

    /**
     * 序列化为字节数据
     * @param data 数据
     * @date 2020/4/22 9:31
     * @return byte[]
     **/
    byte[] serializeRaw(Object data);

    /**
     * 序列化为字符串数据
     * @param data 数据
     * @date 2020/4/22 9:32
     * @return java.lang.String
     **/
    String serialize(Object data);

    /**
     * 反序列字符串数据
     * @param content 序列化字符串
     * @date 2020/4/22 9:32
     * @return T
     **/
    <T> T deserialize(String content);

    /**
     * 反序列字节数据
     * @param content 序列化字节数据
     * @date 2020/4/22 9:32
     * @return T
     **/
    <T> T deserialize(byte[] content);
}
