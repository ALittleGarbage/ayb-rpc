package com.ayb.core.compress.impl;

import com.ayb.core.compress.Compress;

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
