package com.ayb.rpc.core.rpc.client;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保存已创建的channel
 *
 * @author ayb
 * @date 2023/6/4
 */
public class ChannelProvider {

    private final static Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    public static Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (CHANNEL_MAP.containsKey(key)) {
            Channel channel = CHANNEL_MAP.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                CHANNEL_MAP.remove(key);
            }
        }
        return null;
    }

    public static void add(InetSocketAddress inetSocketAddress, Channel channel) {
        String key = inetSocketAddress.toString();
        CHANNEL_MAP.put(key, channel);
    }

    public static void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        CHANNEL_MAP.remove(key);
    }
}
