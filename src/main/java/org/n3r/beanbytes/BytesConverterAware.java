package org.n3r.beanbytes;

import java.lang.reflect.Field;

import org.n3r.beanbytes.impl.ByteBean;

public interface BytesConverterAware<T> {
    void setField(Field field);

    byte[] encode(byte[] bytes, T value);

    ByteBean decode(byte[] bytes, int offset);

}
