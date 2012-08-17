package org.n3r.beanbytes;

import java.lang.reflect.Field;

public interface BytesConverterAware<T> {
    BytesConverterAware<T> setField(Field field);

    Field getField();

    byte[] encode(T value, StringBuilder printer);

    ParseBean<T> decode(byte[] bytes, int offset);
}
