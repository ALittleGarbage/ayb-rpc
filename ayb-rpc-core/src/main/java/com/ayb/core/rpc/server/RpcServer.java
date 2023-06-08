package com.ayb.core.rpc.server;

import com.ayb.common.exception.AybRpcException;
import com.ayb.common.extension.ExtensionLoader;
import com.ayb.common.utils.RuntimeUtils;
import com.ayb.common.utils.ServerAddressUtils;
import com.ayb.core.config.RpcServerConfig;
import com.ayb.core.registry.ServiceRegister;
import com.ayb.core.rpc.Server;
import com.ayb.core.rpc.codec.RpcDecoder;
import com.ayb.core.rpc.codec.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * rpcServer
 *
 * @author ayb
 * @date 2023/6/3
 */
@Slf4j
public class RpcServer implements Server {

    private final ServiceRegister serviceRegister;

    private final RpcServerConfig rpcServerConfig;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private DefaultEventExecutorGroup serviceHandlerGroup;

    private Channel channel;

    public RpcServer(RpcServerConfig rpcServerConfig) {
        this.rpcServerConfig = rpcServerConfig;

        serviceRegister = ExtensionLoader
                .getExtensionLoader(ServiceRegister.class)
                .getExtension(rpcServerConfig.getRegistryType().getName());
    }

    @Override
    public void registerService(String serviceName, Object service) {
        serviceRegister.registerService(
                serviceName,
                service,
                ServerAddressUtils.getServerAddress(rpcServerConfig.getPort()),
                rpcServerConfig.getRegistryServerAddress());
    }

    @Override
    public void start() {
        bossGroup = useEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup(1);
        workerGroup = useEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        serviceHandlerGroup = new DefaultEventExecutorGroup(
                RuntimeUtils.getCpuCount(),
                Executors.defaultThreadFactory());

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, rpcServerConfig.isEnableNagle())
                    .childOption(ChannelOption.SO_KEEPALIVE, rpcServerConfig.isEnableKeepAlive())
                    .option(ChannelOption.SO_BACKLOG, rpcServerConfig.getMaxQueueLength())
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 当客户端第一次进行请求的时候才会进行初始化
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            //30秒之内没有收到客户端请求的话就关闭连接
                            p.addLast(new IdleStateHandler(rpcServerConfig.getReaderIdleTimeSeconds(), 0, 0, TimeUnit.SECONDS));
                            p.addLast(new RpcEncoder(rpcServerConfig.getRpcCodecConfig()));
                            p.addLast(new RpcDecoder(rpcServerConfig.getRpcCodecConfig()));
                            p.addLast(serviceHandlerGroup, new RpcServerHandler(rpcServerConfig.getRpcCodecConfig(), serviceRegister));
                        }
                    });

            // 绑定端口，异步绑定
            ChannelFuture f = bootstrap.bind(rpcServerConfig.getPort());
            this.channel = f.channel();
            // 等待服务端监听端口关闭
            f.channel().closeFuture();
        } catch (Exception e) {
            log.error("启动server时发生错误,原因:{}", e.getMessage());
            AybRpcException.cast("启动server时发生错误,原因:" + e.getMessage());
        }
    }

    @Override
    public void close() {
        if (channel != null) {
            channel.close();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (serviceHandlerGroup != null) {
            serviceHandlerGroup.shutdownGracefully();
        }
        registerShutdownHook();
    }

    private void registerShutdownHook() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            serviceRegister.clearAllService(
                    ServerAddressUtils.getServerAddress(rpcServerConfig.getPort()),
                    rpcServerConfig.getRegistryServerAddress());
        }));
    }

    private boolean useEpoll() {
        return SystemUtils.IS_OS_LINUX && Epoll.isAvailable();
    }
}
