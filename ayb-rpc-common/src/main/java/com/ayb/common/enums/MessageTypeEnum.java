package com.ayb.common.enums;

/**
 * 消息类型
 *
 * @author ayb
 * @date 2023/6/5
 */
public enum MessageTypeEnum {

    /**
     * 请求类型
     */
    REQUEST((byte) 1, "Request"),

    /**
     * 响应类型
     */
    RESPONSE((byte) 2, "Response"),

    /**
     * 心跳请求
     */
    HEARTBEAT_REQUEST((byte) 3, "Ping"),

    /**
     * 心跳响应
     */
    HEARTBEAT_RESPONSE((byte) 4, "Pong");

    private byte code;
    private String name;

    MessageTypeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MessageTypeEnum getMessageType(byte code) {
        for (MessageTypeEnum m : MessageTypeEnum.values()) {
            if (m.getCode() == code) {
                return m;
            }
        }

        return null;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
