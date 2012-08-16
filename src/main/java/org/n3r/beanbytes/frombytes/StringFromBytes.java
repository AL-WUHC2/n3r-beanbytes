package org.n3r.beanbytes.frombytes;

import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.converter.JCStringVarLenConverter;
import org.n3r.beanbytes.impl.BaseBytes;

@JCBindType(String.class)
public class StringFromBytes extends BaseBytes<String> implements FromBytesAware<String> {

    @Override
    public ParseBean<String> fromBytes(byte[] bytes, Class<?> class1, int offset) {
        return getConverter(JCStringVarLenConverter.class).decode(bytes, offset);
    }

}
