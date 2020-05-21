package com.duanxin.rabbit.common.serializer;

/**
 * 序列化工厂
 * @author duanxin
 * @version 1.0
 * @date 2020/4/22 9:27
 */
public interface SerializerFactory {

    /**
     * 序列化
     * @date 2020/4/22 9:29
     * @return com.duanxin.rabbit.common.serializer.Serializer
     **/
    Serializer create();

}
