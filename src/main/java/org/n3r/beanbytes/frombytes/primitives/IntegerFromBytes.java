package org.n3r.beanbytes.frombytes.primitives;

import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;

@JCBindType(Integer.class)
public class IntegerFromBytes extends BaseBytes<Integer> implements FromBytesAware<Integer> {

    @Override
    public ParseBean<Integer> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        return new ParseBean<Integer>(RByte.toInt(bytes, offset), 4);
    }
}
