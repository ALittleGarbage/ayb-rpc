package com.ayb.rpc.common.enums;

/**
 * 注册中心类型
 *
 * @author ayb
 * @date 2023/6/5
 */
public enum RegistryTypeEnum {
    /**
     * Nacos
     */
    NACOS((byte) 1, "Nacos");

    private byte code;
    private String name;

    RegistryTypeEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(byte code) {
        for (RegistryTypeEnum r : RegistryTypeEnum.values()) {
            if (r.getCode() == code) {
                return r.getName();
            }
        }
        return null;
    }

    public static RegistryTypeEnum getByName(String name) {
        for (RegistryTypeEnum r : RegistryTypeEnum.values()) {
            if (r.getName().equals(name)) {
                return r;
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
