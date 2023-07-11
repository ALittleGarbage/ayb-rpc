package com.ayb.rpc.common.enums;

/**
 * 负载均衡策略
 *
 * @author ayb
 * @date 2023/6/5
 */
public enum LoadBalanceRuleEnum {

    /**
     * 随机
     */
    RANDOM((byte) 1, "Random"),

    /**
     * 轮询
     */
    ROUND((byte) 2, "Round"),

    /**
     * 一致性hash
     */
    CONSISTENT_HASH((byte) 3, "Hash");

    private byte code;
    private String name;

    LoadBalanceRuleEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(byte code) {
        for (LoadBalanceRuleEnum l : LoadBalanceRuleEnum.values()) {
            if (l.getCode() == code) {
                return l.getName();
            }
        }

        return null;
    }

    public static LoadBalanceRuleEnum getByName(String name) {
        for (LoadBalanceRuleEnum l : LoadBalanceRuleEnum.values()) {
            if (l.getName().equals(name)) {
                return l;
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
