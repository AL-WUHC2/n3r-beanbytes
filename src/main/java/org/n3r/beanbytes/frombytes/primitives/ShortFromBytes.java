package org.n3r.beanbytes.frombytes.primitives;

import org.n3r.beanbytes.FromByteBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;

@JCBindType(Short.class)
public class ShortFromBytes extends BaseBytes<Short> implements FromBytesAware<Short> {

    @Override
    public FromByteBean<Short> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        return new FromByteBean<Short>(RByte.toShort(bytes, offset), 2);
    }

}
