package com.ayb.rpc.core.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * rpc消息包
 *
 * @author ayb
 * @date 2023/6/1
 */
@Data
public class RpcMessage implements Serializable {

    private static final long serialVersionUID = 1849230072192217291L;

    /**
     * 消息类型
     */
    private Byte messageType;

    /**
     * 请求id
     */
    private Long requestId;

    /**
     * 序列化类型
     */
    private Byte codec;

    /**
     * 压缩类型
     */
    private Byte compress;

    /**
     * 请求数据
     */
    private Object data;
}
