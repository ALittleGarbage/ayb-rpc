package com.ayb.rpc.common.extension;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ayb
 * @date 2023/6/5
 */
@Slf4j
public class Holder<T> {

    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

}