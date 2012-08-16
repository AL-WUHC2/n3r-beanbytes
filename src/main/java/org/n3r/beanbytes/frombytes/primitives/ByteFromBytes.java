package org.n3r.beanbytes.frombytes.primitives;

import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;

@JCBindType(Byte.class)
public class ByteFromBytes extends BaseBytes<Byte> implements FromBytesAware<Byte> {

    @Override
    public ParseBean<Byte> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        return new ParseBean<Byte>(bytes[offset], 1);
    }

}
