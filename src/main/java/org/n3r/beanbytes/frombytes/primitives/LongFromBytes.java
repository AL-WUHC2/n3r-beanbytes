package org.n3r.beanbytes.frombytes.primitives;

import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;

@JCBindType(Long.class)
public class LongFromBytes extends BaseBytes<Long> implements FromBytesAware<Long> {

    @Override
    public ParseBean<Long> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        return new ParseBean<Long>(RByte.toLong(bytes, offset), 8);
    }
}
