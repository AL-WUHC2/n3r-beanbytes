package org.n3r.beanbytes.tobytes;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.converter.JCStringVarLenConverter;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RStr;

@JCBindType(String.class)
public class StringToBytes extends BaseBytes<String> implements ToBytesAware<String> {
    @Override
    public byte[] toBytes(String str, StringBuilder printer) {
        RStr.append(printer, str);

        return getConverter(JCStringVarLenConverter.class).encode(str);
    }
}
