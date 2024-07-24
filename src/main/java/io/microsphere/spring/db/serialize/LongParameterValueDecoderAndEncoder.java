package io.microsphere.spring.db.serialize;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

public class LongParameterValueDecoderAndEncoder implements ParameterValueEncoderAndDecoder<Long> {




    @Override
    public byte[] getBytes(Long aLong) {
        long longValue = aLong.longValue();
        byte[] bytes = new byte[]{
                (byte) longValue,
                (byte) (longValue >> 8),
                (byte) (longValue >> 16),
                (byte) (longValue >> 24),
                (byte) (longValue >> 32),
                (byte) (longValue >> 40),
                (byte) (longValue >> 48),
                (byte) (longValue >> 56)
        };
        return bytes;
    }

    @Override
    public String getString(Long s) {
        return String.valueOf(s);
    }

    @Override
    public Long getObject(byte[] bytes) {
        long longValue = ((long) bytes[7] << 56)
                | ((long) bytes[6] & 0xff) << 48
                | ((long) bytes[5] & 0xff) << 40
                | ((long) bytes[4] & 0xff) << 32
                | ((long) bytes[3] & 0xff) << 24
                | ((long) bytes[2] & 0xff) << 16
                | ((long) bytes[1] & 0xff) << 8
                | ((long) bytes[0] & 0xff);
        return longValue;
    }

    @Override
    public Long getObject(String String) {
        return Long.valueOf(String);
    }
}
