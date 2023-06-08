package com.ayb.core.rpc.client;

import com.ayb.common.enums.MessageTypeEnum;
import com.ayb.common.exception.AybRpcException;
import com.ayb.common.extension.ExtensionLoader;
import com.ayb.core.config.RpcClientConfig;
import com.ayb.core.loadbalance.LoadBalance;
import com.ayb.core.registry.ServiceDiscovery;
import com.ayb.core.rpc.Client;
import com.ayb.core.rpc.codec.RpcDecoder;
import com.ayb.core.rpc.codec.RpcEncoder;
import com.ayb.core.rpc.protocol.RpcMessage;
import com.ayb.core.rpc.protocol.RpcRequest;
import com.ayb.core.rpc.protocol.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * rpcClient
 *
 * @author ayb
 * @date 2023/6/4
 */
@Slf4j
public class RpcClient implements Client {

    private final RpcClientConfig rpcClientConfig;

    private final ServiceDiscovery serviceDiscovery;

    private final LoadBalance loadBalance;

    private Bootstrap bootstrap;

    private EventLoopGroup eventLoopGroup;

    public RpcClient(RpcClientConfig rpcClientConfig) {
        this.rpcClientConfig = rpcClientConfig;

        this.serviceDiscovery = ExtensionLoader
                .getExtensionLoader(ServiceDiscovery.class)
                .getExtension(rpcClientConfig.getRegistryType().getName());

        this.loadBalance = ExtensionLoader
                .getExtensionLoader(LoadBalance.class)
                .getExtension(rpcClientConfig.getLoadBalanceRule().getName());
    }

    @Override
    public void start() {
        eventLoopGroup = useEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(useEpoll() ? EpollSocketChannel.class : NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) TimeUnit.SECONDS.toMillis(rpcClientConfig.getConnectTimeoutSeconds()))
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new IdleStateHandler(0, rpcClientConfig.getHeartbeatSeconds(), 0, TimeUnit.SECONDS));
                        p.addLast(new RpcEncoder(rpcClientConfig.getRpcCodecConfig()));
                        p.addLast(new RpcDecoder(rpcClientConfig.getRpcCodecConfig()));
                        p.addLast(new RpcClientHandler(rpcClientConfig.getRpcCodecConfig()));
                    }
                });
    }

    @Override
    public RpcResponse sendRpcRequest(RpcRequest rpcRequest) {
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();

        // 获取rpc服务地址
        List<InetSocketAddress> serverAddressList = serviceDiscovery
                .lookupService(rpcRequest.getServiceName(), rpcClientConfig.getRegistryServerAddress());
        // 负载均衡
        InetSocketAddress inetSocketAddress = loadBalance.getServiceAddress(serverAddressList, rpcRequest);
        if (inetSocketAddress == null) {
            AybRpcException.cast("获取rpc服务地址失败");
        }

        // 保存任务
        RpcResponseFuture.add(rpcRequest.getRequestId(), resultFuture);

        // 创建消息包
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setRequestId(rpcRequest.getRequestId());
        rpcMessage.setCompress(rpcClientConfig.getRpcCodecConfig().getCompressType().getCode());
        rpcMessage.setCodec(rpcClientConfig.getRpcCodecConfig().getSerializeType().getCode());
        rpcMessage.setMessageType(MessageTypeEnum.REQUEST.getCode());
        rpcMessage.setData(rpcRequest);

        // 获取channel
        Channel channel = getChannel(inetSocketAddress);
        channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                future.channel().close();
                RpcResponseFuture.fail(rpcRequest.getRequestId(), future.cause());
                log.error("rpcMessage发送失败:{}", future.cause().getMessage());
            }
        });

        try {
            return resultFuture.get(rpcClientConfig.getRequestTimeOutSeconds(), TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("获取响应结果超时");
            RpcResponseFuture.fail(rpcRequest.getRequestId(), e);
            AybRpcException.cast("获取响应结果超时");
        } catch (Exception e) {
            log.error("获取响应结果失败,原因:{}", e.getMessage());
            RpcResponseFuture.fail(rpcRequest.getRequestId(), e);
            AybRpcException.cast(e.getMessage());
        }

        return null;
    }

    @Override
    public void close() {
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully();
        }
    }

    private Channel connect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                completableFuture.complete(future.channel());
            } else {
                completableFuture.completeExceptionally(future.cause());
                log.error("地址:{},连接失败,原因:{}", inetSocketAddress.toString(), future.cause().getMessage());
                AybRpcException.cast(inetSocketAddress + "连接失败,原因:" + future.cause().getMessage());
            }
        });

        try {
            return completableFuture.get();
        } catch (Exception e) {
            log.error("channel获取失败,原因:{}", e.getMessage());
            AybRpcException.cast("channel获取失败,原因:" + e.getMessage());
        }

        return null;
    }

    private Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = ChannelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = connect(inetSocketAddress);
            ChannelProvider.add(inetSocketAddress, channel);
        }
        return channel;
    }

    private boolean useEpoll() {
        return SystemUtils.IS_OS_LINUX && Epoll.isAvailable();
    }
}
