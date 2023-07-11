package com.ayb.rpc.common.utils;

import com.ayb.rpc.common.exception.AybRpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author ayb
 * @date 2023/6/5
 */
@Slf4j
public class ServerAddressUtils {

    public static InetSocketAddress getServerAddress(Integer port) {
        if (port == null) {
            AybRpcException.cast("端口为空");
        }

        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            return InetSocketAddress.createUnresolved(hostAddress, port);
        } catch (UnknownHostException e) {
            log.error("获取本地ip失败,原因:{}", e.getMessage());
            AybRpcException.cast("获取本地ip失败,原因:" + e.getMessage());
        }

        return null;
    }
}
