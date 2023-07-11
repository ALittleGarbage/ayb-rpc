package com.ayb.rpc.core.compress.impl;

import com.ayb.rpc.core.compress.Compress;

/**
 * 不压缩
 *
 * @author ayb
 * @date 2023/6/1
 */
public class NoneCompress implements Compress {
    @Override
    public byte[] compress(byte[] bytes) {
        return bytes;
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return bytes;
    }
}
