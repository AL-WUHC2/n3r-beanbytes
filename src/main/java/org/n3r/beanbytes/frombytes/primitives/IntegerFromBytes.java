package org.n3r.beanbytes.frombytes.primitives;

import org.n3r.beanbytes.FromByteBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;

@JCBindType(Integer.class)
public class IntegerFromBytes extends BaseBytes<Integer> implements FromBytesAware<Integer> {

    @Override
    public FromByteBean<Integer> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        FromByteBean<Integer> result = new FromByteBean<Integer>();
        result.setBean(RByte.toInt(bytes, offset));
        result.setBytesSize(4);
        return result;
    }
}
