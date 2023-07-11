# ayb-rpc

这是一款基于Netty开发的轻量rpc框架

主要功能:
* 遵循阿里巴巴开发手册规范
* 支持的序列化类型:Kryo
* 支持的压缩算法:Gzip
* 支持Nacos注册中心，自动服务注册和发现
* 支持多种负载均衡策略:随机，轮询，一致性hash
* 集成spring-boot自动装配，在spring-boot项目下引入starter包开箱即用
