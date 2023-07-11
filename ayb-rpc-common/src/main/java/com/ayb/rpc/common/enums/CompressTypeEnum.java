package com.ayb.rpc.common.enums;

/**
 * 压缩类型
 *
 * @author ayb
 * @date 2023/6/5
 */
public enum CompressTypeEnum {

    /**
     * 不压缩
     */
    NONE((byte) 1, "None"),

    /**
     * Gzip压缩
     */
    GZIP((byte) 2, "Gzip");

    private byte code;
    private String name;

    CompressTypeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(byte code) {
        for (CompressTypeEnum c : CompressTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.getName();
            }
        }
        return null;
    }

    public static CompressTypeEnum getByName(String name) {
        for (CompressTypeEnum c : CompressTypeEnum.values()) {
            if (c.getName().equals(name)) {
                return c;
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
