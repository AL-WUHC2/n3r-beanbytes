package org.n3r.beanbytes.tobytes.primitives;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;

@JCBindType(Byte.class)
public class ByteToBytes extends BaseBytes<Byte> implements ToBytesAware<Byte> {

    @Override
    public byte[] toBytes(Byte bean, StringBuilder printer) {
        if (printer != null) {
            printer.append(bean);
        }
        return new byte[] { bean };
    }

}
