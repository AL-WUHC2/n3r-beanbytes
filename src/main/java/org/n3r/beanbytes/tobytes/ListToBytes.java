package org.n3r.beanbytes.tobytes;

import java.util.List;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.converter.JCListConverter;
import org.n3r.beanbytes.impl.BaseBytes;

@JCBindType(List.class)
public class ListToBytes extends BaseBytes<List<Object>> implements ToBytesAware<List<Object>> {
    @Override
    public byte[] toBytes(List<Object> bean, StringBuilder printer) {
        return getConverter(JCListConverter.class).encode(bean, printer);
    }
}
