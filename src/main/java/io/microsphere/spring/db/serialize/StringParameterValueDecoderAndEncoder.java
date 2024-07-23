package io.microsphere.spring.db.serialize;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

public class StringParameterValueDecoderAndEncoder implements ParameterValueEncoderAndDecoder<String>{
    private final String charSet;
    private final String value;

    public StringParameterValueDecoderAndEncoder(String charSet, String value) {
        this.charSet = charSet;
        this.value = value;
    }

    @Override
    public byte[] getBytes(String s) {
        try {
            return StringUtils.getBytes(value, charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getString(String s) {
        return s;
    }

    @Override
    public String getObject(byte[] bytes) {
        return null;
    }

    @Override
    public String getObject(String String) {
        return null;
    }
}
