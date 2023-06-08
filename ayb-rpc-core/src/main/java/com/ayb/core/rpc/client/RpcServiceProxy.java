package com.ayb.core.rpc.client;

import com.ayb.common.enums.ResponseStatusTypeEnum;
import com.ayb.common.exception.AybRpcException;
import com.ayb.common.utils.IdWorkerUtils;
import com.ayb.core.rpc.Client;
import com.ayb.core.rpc.protocol.RpcRequest;
import com.ayb.core.rpc.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 获取service接口的代理
 *
 * @author ayb
 * @date 2023/6/5
 */
public class RpcServiceProxy implements InvocationHandler {

    private final Client client;

    public RpcServiceProxy(Client client) {
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(IdWorkerUtils.getInstance().nextId());
        rpcRequest.setServiceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setParamTypes(method.getParameterTypes());

        RpcResponse rpcResponse = client.sendRpcRequest(rpcRequest);

        check(rpcRequest, rpcResponse);

        return rpcResponse.getBody();
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    private void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        if (rpcResponse == null) {
            AybRpcException.cast("响应结果为空");
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            AybRpcException.cast("requestId不匹配");
        }

        if (rpcResponse.getStatus().equals(ResponseStatusTypeEnum.FAIL.getCode())) {
            AybRpcException.cast("状态码为" + rpcResponse.getStatus() + ",原因:" + rpcResponse.getBody().toString());
        }
    }
}
