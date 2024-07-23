package io.microsphere.spring.db.serialize;

public class NullParameterValueEncoderAndDecoder  implements ParameterValueEncoderAndDecoder{

    @Override
    public Object getObject(byte[] bytes) {
        return null;
    }

    @Override
    public Object getObject(String String) {
        return null;
    }

    @Override
    public byte[] getBytes(Object o) {
        return new byte[0];
    }

    @Override
    public String getString(Object o) {
        return null;
    }
}
