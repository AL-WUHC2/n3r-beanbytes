package org.n3r.beanbytes.tobytes.primitives;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RStr;

@JCBindType(Byte.class)
public class ByteToBytes extends BaseBytes<Byte> implements ToBytesAware<Byte> {

    @Override
    public byte[] toBytes(Byte bean, StringBuilder printer) {
        RStr.append(printer, bean);

        return new byte[] { bean };
    }

}
