package io.microsphere.spring.db.serialize;

import com.mysql.cj.protocol.ValueEncoder;

import java.io.InputStream;

public class InputStreamParameterValueEncoderAndDecoder implements ParameterValueEncoderAndDecoder<InputStream>{

    @Override
    public InputStream getObject(byte[] bytes) {
        return null;
    }

    @Override
    public InputStream getObject(String string) {
        return null;
    }

    @Override
    public byte[] getBytes(InputStream inputStream) {
        return new byte[0];
    }

    @Override
    public String getString(InputStream inputStream) {
        return null;
    }
}
