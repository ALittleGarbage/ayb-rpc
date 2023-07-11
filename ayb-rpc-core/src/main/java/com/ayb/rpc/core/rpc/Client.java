package com.ayb.rpc.core.rpc;

import com.ayb.rpc.core.rpc.protocol.RpcRequest;
import com.ayb.rpc.core.rpc.protocol.RpcResponse;

/**
 * 客户端接口
 *
 * @author ayb
 * @date 2023/6/5
 */
public interface Client {

    /**
     * 启动客户端
     */
    void start();

    /**
     * 发送rpc请求
     *
     * @param rpcRequest 请求参数
     * @return rpc响应
     */
    RpcResponse sendRpcRequest(RpcRequest rpcRequest);

    /**
     * 关闭客户端
     */
    void close();
}
