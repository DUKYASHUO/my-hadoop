package com.hadoop.app;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CodecPool;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.Compressor;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;

public class PooledStreamCompressor {

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        String codecClassname = args[0];
        Class<?> codecClass = Class.forName(codecClassname);
        Configuration conf = new Configuration();
        CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);

        Compressor compressor = null;

        try {
            compressor = CodecPool.getCompressor(codec);
            CompressionOutputStream outputStream = codec.createOutputStream(System.out, compressor);
            IOUtils.copyBytes(System.in, outputStream, 4096, false);
            outputStream.finish();
        } finally {
            CodecPool.returnCompressor(compressor);
        }
    }
}
