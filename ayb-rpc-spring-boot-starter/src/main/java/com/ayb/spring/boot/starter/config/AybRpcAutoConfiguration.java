package com.ayb.spring.boot.starter.config;

import com.ayb.common.enums.CompressTypeEnum;
import com.ayb.common.enums.LoadBalanceRuleEnum;
import com.ayb.common.enums.RegistryTypeEnum;
import com.ayb.common.enums.SerializeTypeEnum;
import com.ayb.common.exception.AybRpcException;
import com.ayb.core.config.RpcClientConfig;
import com.ayb.core.config.RpcCodecConfig;
import com.ayb.core.config.RpcServerConfig;
import com.ayb.core.rpc.Client;
import com.ayb.core.rpc.Server;
import com.ayb.core.rpc.client.RpcClient;
import com.ayb.core.rpc.server.RpcServer;
import com.ayb.spring.boot.starter.config.properties.RpcClientProperties;
import com.ayb.spring.boot.starter.config.properties.RpcCommonProperties;
import com.ayb.spring.boot.starter.config.properties.RpcServerProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * ayb-rpc自动配置类
 *
 * @author ayb
 * @date 2023/6/5
 */
@Configuration
@EnableConfigurationProperties({RpcServerProperties.class, RpcClientProperties.class, RpcCommonProperties.class})
public class AybRpcAutoConfiguration {

    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnMissingBean(Server.class)
    public Server rpcServer(RpcServerProperties serverProperties, RpcCommonProperties commonProperties) {

        RpcCodecConfig rpcCodecConfig = new RpcCodecConfig(
                commonProperties.getMagicNumber().getBytes(StandardCharsets.UTF_8),
                commonProperties.getVersion(),
                commonProperties.getMaxFrameLength(),
                CompressTypeEnum.getByName(commonProperties.getCompressType()),
                SerializeTypeEnum.getByName(commonProperties.getSerializeType()));

        RpcServerConfig rpcServerConfig = new RpcServerConfig(
                serverProperties.getPort(),
                serverProperties.getReaderIdleTimeSeconds(),
                serverProperties.getEnableNagle(),
                serverProperties.getEnableKeepAlive(),
                serverProperties.getMaxQueueLength(),
                RegistryTypeEnum.getByName(commonProperties.getRegistryType()),
                commonProperties.getRegistryServerAddress(),
                rpcCodecConfig);

        return new RpcServer(rpcServerConfig);
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnMissingBean(Client.class)
    public Client rpcClient(RpcClientProperties clientProperties, RpcCommonProperties commonProperties) {
        if (StringUtils.isEmpty(commonProperties.getRegistryServerAddress())) {
            AybRpcException.cast("注册中心为空");
        }

        RpcCodecConfig rpcCodecConfig = new RpcCodecConfig(
                commonProperties.getMagicNumber().getBytes(StandardCharsets.UTF_8),
                commonProperties.getVersion(),
                commonProperties.getMaxFrameLength(),
                CompressTypeEnum.getByName(commonProperties.getCompressType()),
                SerializeTypeEnum.getByName(commonProperties.getSerializeType()));

        RpcClientConfig rpcClientConfig = new RpcClientConfig(
                clientProperties.getConnectTimeoutSeconds(),
                clientProperties.getHeartbeatSeconds(),
                clientProperties.getRequestTimeOutSeconds(),
                LoadBalanceRuleEnum.getByName(clientProperties.getLoadBalanceRule()),
                RegistryTypeEnum.getByName(commonProperties.getRegistryType()),
                commonProperties.getRegistryServerAddress(),
                rpcCodecConfig);

        return new RpcClient(rpcClientConfig);
    }
}
