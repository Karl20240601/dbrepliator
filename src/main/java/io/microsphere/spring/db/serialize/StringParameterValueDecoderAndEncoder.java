package io.microsphere.spring.db.serialize;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

public class StringParameterValueDecoderAndEncoder implements ParameterValueEncoderAndDecoder<String> {
    private final String defaultCharset = "utf-8";


    @Override
    public byte[] getBytes(String value) {
        try {
            return StringUtils.getBytes(value, defaultCharset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getString(String s) {
        return s;
    }

    @Override
    public String getObject(byte[] bytes) {
        try {
            return new String(bytes, defaultCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getObject(String String) {
        return String;
    }
}
