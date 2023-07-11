package com.ayb.rpc.core.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * rpc请求参数
 *
 * @author ayb
 * @date 2023/6/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -6258834411184050015L;

    private Long requestId;

    private String serviceName;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] parameters;
}
