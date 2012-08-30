package org.n3r.beanbytes.frombytes;

import org.apache.commons.lang3.tuple.Pair;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.converter.JCPairFixLenConverter;
import org.n3r.beanbytes.impl.BaseBytes;

@JCBindType(Pair.class)
public class PairFromBytes extends BaseBytes<Pair<String, String>> implements FromBytesAware<Pair<String, String>> {

    @Override
    public ParseBean<Pair<String, String>> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        return getConverter(JCPairFixLenConverter.class).decode(bytes, offset);
    }

}
