package org.n3r.beanbytes.frombytes.primitives;

import org.n3r.beanbytes.FromByteBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;

@JCBindType(Long.class)
public class LongFromBytes extends BaseBytes<Long> implements FromBytesAware<Long> {

    @Override
    public FromByteBean<Long> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        FromByteBean<Long> result = new FromByteBean<Long>();
        result.setBean(RByte.toLong(bytes, offset));
        result.setBytesSize(8);
        return result;
    }
}
