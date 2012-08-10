package org.n3r.beanbytes.frombytes.primitives;

import org.n3r.beanbytes.FromByteBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;

@JCBindType(Byte.class)
public class ByteFromBytes extends BaseBytes<Byte> implements FromBytesAware<Byte> {

    @Override
    public FromByteBean<Byte> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        FromByteBean<Byte> result = new FromByteBean<Byte>();
        result.setBean(bytes[offset]);
        result.setBytesSize(1);
        return result;
    }

}
