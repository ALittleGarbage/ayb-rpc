package com.ayb.common.exception;

/**
 * 自定义异常
 *
 * @author ayb
 * @date 2023/6/5
 */
public class AybRpcException extends RuntimeException {

    public AybRpcException() {
        super();
    }

    public AybRpcException(String cause) {
        super(cause);
    }

    public static AybRpcException cast(String cause) {
        throw new AybRpcException(cause);
    }
}
