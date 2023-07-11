package com.ayb.rpc.core.compress.impl;


import com.ayb.rpc.common.exception.AybRpcException;
import com.ayb.rpc.core.compress.Compress;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Gzip压缩
 *
 * @author ayb
 * @date 2023/6/3
 */
@Slf4j
public class GzipCompress implements Compress {


    private static final int BUFFER_SIZE = 1024 * 4;

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            AybRpcException.cast("数据为空");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(bytes);
            gzip.flush();
            gzip.finish();
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Gzip压缩时发生错误,原因:{}", e.getMessage());
            AybRpcException.cast("Gzip压缩时发生错误,原因:" + e.getMessage());
        }

        return null;
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            AybRpcException.cast("数据为空");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gunzip.read(buffer)) > -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (Exception e) {
            log.error("Gzip解压时发生错误,原因:{}", e.getMessage());
            AybRpcException.cast("Gzip解压时发生错误,原因:" + e.getMessage());
        }

        return null;
    }
}
