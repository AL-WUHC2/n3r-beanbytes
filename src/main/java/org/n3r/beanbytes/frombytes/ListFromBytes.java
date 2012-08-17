package org.n3r.beanbytes.frombytes;

import java.util.List;

import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.converter.JCListConverter;
import org.n3r.beanbytes.impl.BaseBytes;

@JCBindType(List.class)
public class ListFromBytes extends BaseBytes<List<Object>> implements FromBytesAware<List<Object>> {
    @Override
    public ParseBean<List<Object>> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        return getConverter(JCListConverter.class).decode(bytes, offset);
    }
}
