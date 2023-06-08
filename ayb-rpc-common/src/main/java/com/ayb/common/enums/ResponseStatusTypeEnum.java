package com.ayb.common.enums;

/**
 * rpc响应状态码
 *
 * @author ayb
 * @date 2023/6/5
 */
public enum ResponseStatusTypeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "success"),

    /**
     * 失败
     */
    FAIL(500, "fail");

    private final int code;
    private final String message;

    ResponseStatusTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
