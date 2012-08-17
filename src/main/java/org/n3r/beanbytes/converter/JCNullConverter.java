package org.n3r.beanbytes.converter;

import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.TypeConverter;

public class JCNullConverter<T> extends TypeConverter<T> {
    @Override
    public byte[] encode(T value, StringBuilder printer) {
        return new byte[0];
    }

    @Override
    public ParseBean<T> decode(byte[] bytes, int offset) {
        return new ParseBean<T>(null, 0);
    }
}
