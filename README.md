# ayb-rpc

### 1.什么是RPC？

引用GPT的话：

RPC简称Remote Procedure Call（远程过程调用）是一种用于实现分布式系统间通信的协议。**它允许一个计算机程序调用另一个计算机上的函数或方法，就像调用本地函数一样，隐藏了底层网络通信的复杂性。**

例如一些常见的RPC框架：gRPC、Thrift、Dubbo、Spring Cloud Netflix

### 2.为什么需要RPC？

引用GPT的话：

1. **在分布式系统中，各个节点之间需要进行通信和协作**。RPC提供了一种方便的方式，使得不同节点上的应用程序可以通过调用远程方法来实现交互和共享资源。它使得分布式系统的开发和维护更加简单和高效。

2. **RPC允许将功能逻辑封装为可调用的远程方法，提供了更好的代码复用和模块化的能力**。这样可以将常用的功能或者业务逻辑封装为服务，供多个应用程序共享和重用。
3. **RPC框架通常会优化网络通信和序列化/反序列化的性能，使得远程调用的开销尽可能小**。这帮助提高了分布式系统的效率和性能。
4. **使用RPC，开发人员可以将网络通信的细节抽象化，专注于业务逻辑的实现**。RPC框架会处理底层通信协议、连接管理和错误处理等操作，使开发人员能够更专注于核心业务的开发。
5. 等等

### 3.RPC的基本原理

RPC的原理就是调用远程方法就像调用本地方法一样，关键是如何实现呢？

我现在有几个问题：

1. 客户端如何知道要请求方法的服务端地址？
2. 客户端需要传递什么信息呢？
3. 怎样进行通信呢？
4. 服务端如何执行相应调用的方法呢？



#### 3.1 客户端如何知道服务端的地址？

我相信大家都能想出来，对！那就是**注册中心！！**（我觉得其实使用Rieds也是可以的）

1. 服务端将实现的接口名以及自身的地址注册到注册中心
2. 客户端通过接口名获取到实例列表，通过负载均衡获取一个服务端地址

常见的注册中心有：Zookeeper、Eureka、Nacos等等

而我选择的是Nacos



#### 3.2 客户端需要传递什么信息呢？

这个就很简单了

* **接口名**
* **方法名**
* **方法参数类型列表**
* **方法参数值列表**

如何实现呢？那就是代理

```java
public interface InvocationHandler {
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
```

通过实现InvocationHandler的invoke方法获取到相应的接口名和方法名



#### 3.3 怎样进行通信呢？

这里我选择的是使用Netty框架进行网络通信

**我为什么选择Netty？**

Netty是一个基于Java的异步事件驱动的网络应用程序框架，用于快速开发高性能、可扩展的网络服务器和客户端。它提供了简单、灵活、高性能的网络编程接口，可以用于构建各种网络协议和应用。

**Netty的特点：**

- 高并发：基于 NIO开发，对比于 BIO，他的并发性能得到了很大提高；
- 传输快：传输依赖于零拷贝特性，尽量减少不必要的内存拷贝，实现了更高效率的传输；
- 封装好：原生的封装了 NIO 操作的很多细节，提供了易于使用调用接口。



#### 3.4 服务端如何执行相应调用的方法呢？

**很明显那就是反射**

在服务端进行服务注册时，同时将已注册的接口名以及对应的实例对象保存在一个Map中，通过接口名拿到对应的实例对象，使用反射执行相应的方法



#### 3.3总体流程

公共接口：

```java
package com.ayb.service;

public interface HelloService {

    String sayHello(String name);
}
```

服务端实现：

```java
package com.ayb.example.impl;

import com.ayb.service.HelloService;

public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "你好!" + name;
    }
}
```

![ayb-rpc流程](https://github.com/ALittleGarbage/ayb-rpc/assets/86274455/3f4b6e09-f8d7-41ac-b714-ddf64ef60920)


### 4.RPC的实现

我基于以上流程实现了基于Netty开发的轻量RPC框架

如果想要深入了解的话可以查看我的项目`ayb-rpc`，如有错误希望可以指出，欢迎打扰

项目地址：https://github.com/ALittleGarbage/ayb-rpc

主要特点：

- 遵循阿里巴巴开发手册规范
- 支持的序列化类型:Kryo
- 支持的压缩算法:Gzip
- 支持Nacos注册中心，实现服务注册和发现
- 支持多种负载均衡策略:随机，轮询，一致性hash
- 集成spring-boot自动装配，在spring-boot项目下引入starter包开箱即用


