package com.ayb.core.compress;

import com.ayb.common.annotation.SPI;

import java.io.IOException;

/**
 * 压缩接口
 *
 * @author ayb
 * @date 2023/6/1
 */
@SPI
public interface Compress {

    /**
     * 压缩
     *
     * @param bytes 原始字节数组
     * @return 压缩后的字节数组
     * @throws IOException
     */
    byte[] compress(byte[] bytes) throws IOException;

    /**
     * 解压
     *
     * @param bytes 压缩后的字节数组
     * @return 原始字节数组
     * @throws IOException
     */
    byte[] decompress(byte[] bytes) throws IOException;
}
