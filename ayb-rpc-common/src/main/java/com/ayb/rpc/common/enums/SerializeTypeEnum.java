package com.ayb.rpc.common.enums;

/**
 * 序列化类型
 *
 * @author ayb
 * @date 2023/6/5
 */
public enum SerializeTypeEnum {

    /**
     * Kryo序列化
     */
    KRYO((byte) 1, "Kryo"),

    /**
     * protostuff序列化
     */
    PROTOSTUFF((byte) 2, "Protostuff"),

    /**
     * JSON序列化
     */
    JSON((byte) 3, "JSON"),

    /**
     * JDK序列化
     */
    JDK((byte) 4, "JDK"),

    /**
     * 自定义序列化类型，需使用SPI
     */
    CUSTOM((byte) 5, "custom");

    private byte code;
    private String name;

    SerializeTypeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(byte code) {
        for (SerializeTypeEnum s : SerializeTypeEnum.values()) {
            if (s.getCode() == code) {
                return s.getName();
            }
        }
        return null;
    }

    public static SerializeTypeEnum getByName(String name) {
        for (SerializeTypeEnum s : SerializeTypeEnum.values()) {
            if (s.getName().equals(name)) {
                return s;
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
