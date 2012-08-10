package org.n3r.beanbytes.frombytes.primitives;

import org.n3r.beanbytes.FromByteBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;

@JCBindType(Boolean.class)
public class BooleanFromBytes extends BaseBytes<Boolean> implements FromBytesAware<Boolean> {

    @Override
    public FromByteBean<Boolean> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        FromByteBean<Boolean> result = new FromByteBean<Boolean>();
        result.setBean(RByte.toBoolean(RByte.subBytes(bytes, offset, 1)));
        result.setBytesSize(1);
        return result;
    }

}
