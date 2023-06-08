package com.ayb.common.utils;

/**
 * @author ayb
 * @date 2023/6/5
 */
public class RuntimeUtils {
    /**
     * 获取CPU的核心数
     *
     * @return cpu的核心数
     */
    public static int getCpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }
}