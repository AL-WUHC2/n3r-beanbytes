package org.n3r.beanbytes;

import java.lang.reflect.Field;

public interface BytesConverterAware<T> {
    void setField(Field field);

    byte[] encode(T value);

    ParseBean<T> decode(byte[] bytes, int offset);

}
