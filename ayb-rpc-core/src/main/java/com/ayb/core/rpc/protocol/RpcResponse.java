package com.ayb.core.rpc.protocol;

import com.ayb.common.enums.ResponseStatusTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * rpc响应结果
 *
 * @author ayb
 * @date 2023/6/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 5317513133075573059L;

    private Long requestId;

    private Integer status;

    private Object body;

    public static RpcResponse oK(Long requestId) {
        return new RpcResponse(requestId, ResponseStatusTypeEnum.SUCCESS.getCode(), null);
    }

    public static RpcResponse oK(Long requestId, Object body) {
        return new RpcResponse(requestId, ResponseStatusTypeEnum.SUCCESS.getCode(), body);
    }

    public static RpcResponse fail(Long requestId) {
        return new RpcResponse(requestId, ResponseStatusTypeEnum.FAIL.getCode(), null);
    }

    public static RpcResponse fail(Long requestId, Object body) {
        return new RpcResponse(requestId, ResponseStatusTypeEnum.FAIL.getCode(), body);
    }
}
