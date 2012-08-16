package org.n3r.beanbytes.frombytes.primitives;

import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;

@JCBindType(Boolean.class)
public class BooleanFromBytes extends BaseBytes<Boolean> implements FromBytesAware<Boolean> {

    @Override
    public ParseBean<Boolean> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        return new ParseBean<Boolean>(RByte.toBoolean(RByte.subBytes(bytes, offset, 1)), 1);
    }

}
