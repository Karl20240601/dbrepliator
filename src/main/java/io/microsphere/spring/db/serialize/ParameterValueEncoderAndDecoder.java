package io.microsphere.spring.db.serialize;

import com.mysql.cj.BindValue;
import org.springframework.lang.Nullable;

public interface ParameterValueEncoderAndDecoder<T> {
    T getObject(byte[] bytes);

    T getObject(String string);

    byte[] getBytes(T t);

    String getString(T t);
}
