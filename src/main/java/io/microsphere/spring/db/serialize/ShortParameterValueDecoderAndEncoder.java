package io.microsphere.spring.db.serialize;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

public class ShortParameterValueDecoderAndEncoder implements ParameterValueEncoderAndDecoder<Short> {

    @Override
    public Short getObject(byte[] bytes) {
        return (short) ((bytes[0] << 8) | (bytes[1] & 0xFF));
    }

    @Override
    public Short getObject(String string) {
        return StringUtils.isBlank(string)?null:Short.valueOf(string);
    }

    @Override
    public byte[] getBytes(Short aShort) {
        short shortValue = aShort.shortValue();
        byte[] bytes = new byte[]{
                (byte) (shortValue >>> 8),
                (byte) (shortValue & 0xFF)};
        return bytes;
    }

    @Override
    public String getString(Short aShort) {
        return aShort==null?null:String.valueOf(aShort);
    }
}
