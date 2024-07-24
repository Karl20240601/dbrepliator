package io.microsphere.spring.db.serialize;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

public class IntergerParameterValueDecoderAndEncoder implements ParameterValueEncoderAndDecoder<Integer> {

    @Override
    public Integer getObject(byte[] bytes) {
        int intValue = (0xff & bytes[0]) << 24 |
                (0xff & bytes[1]) << 16 |
                (0xff & bytes[2]) << 8 |
                (0xff & bytes[3]) << 0;
        return intValue;
    }

    @Override
    public Integer getObject(String string) {
        return string==null?null:Integer.valueOf(string);
    }

    @Override
    public byte[] getBytes(Integer integer) {
        int intValue = integer.intValue();
        byte[] bytes = new byte[]{
                (byte) ((intValue >> 24) & 0xff),
                (byte) ((intValue >> 16) & 0xff),
                (byte) ((intValue >> 8) & 0xff),
                (byte) ((intValue >> 0) & 0xff),
        };
        return bytes;
    }

    @Override
    public String getString(Integer integer) {
        return integer==null?null:String.valueOf(integer);
    }
}
