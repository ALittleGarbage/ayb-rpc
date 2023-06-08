package com.ayb.core.rpc.client;

import com.ayb.common.exception.AybRpcException;
import com.ayb.core.rpc.protocol.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异步获取rpc响应
 *
 * @author ayb
 * @date 2023/6/4
 */
public class RpcResponseFuture {

    private static final Map<Long, CompletableFuture<RpcResponse>> RPC_RESPONSE_FUTURE_MAP = new ConcurrentHashMap<>();

    public static void add(Long requestId, CompletableFuture<RpcResponse> rpcResponseFuture) {
        RPC_RESPONSE_FUTURE_MAP.put(requestId, rpcResponseFuture);
    }

    public static void complete(RpcResponse rpcResponse) {
        CompletableFuture<RpcResponse> future = RPC_RESPONSE_FUTURE_MAP.remove(rpcResponse.getRequestId());
        if (future == null) {
            AybRpcException.cast("rpc请求任务不存在");
        }
        future.complete(rpcResponse);
    }

    public static void fail(Long requestId, Throwable cause) {
        CompletableFuture<RpcResponse> future = RPC_RESPONSE_FUTURE_MAP.remove(requestId);
        if (future == null) {
            return;
        }

        future.completeExceptionally(cause);
    }
}
