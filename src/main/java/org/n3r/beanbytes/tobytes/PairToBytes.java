package org.n3r.beanbytes.tobytes;

import org.apache.commons.lang3.tuple.Pair;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.converter.JCPairFixLenConverter;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RStr;

@JCBindType(Pair.class)
public class PairToBytes extends BaseBytes<Pair<String, String>> implements ToBytesAware<Pair<String, String>> {

    @Override
    public byte[] toBytes(Pair<String, String> pair, StringBuilder printer) {
        RStr.append(printer, pair.toString());

        return getConverter(JCPairFixLenConverter.class).encode(pair, printer);
    }

}
