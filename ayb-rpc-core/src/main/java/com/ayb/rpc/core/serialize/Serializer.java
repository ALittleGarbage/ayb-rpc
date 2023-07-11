package com.ayb.rpc.core.serialize;

import com.ayb.rpc.common.annotation.SPI;

/**
 * 序列化接口
 *
 * @author ayb
 * @date 2023/6/1
 */
@SPI
public interface Serializer {

    /**
     * 序列化
     *
     * @param object 实体对象
     * @return 字节数组
     */
    byte[] serialize(Object object);

    /**
     * 反序列化
     *
     * @param bytes 序列化后的字节数组
     * @param clazz clazz
     * @param <T>   类型
     * @return 实体对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
