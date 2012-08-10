package org.n3r.beanbytes.tobytes.primitives;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;

@JCBindType(Long.class)
public class LongToBytes extends BaseBytes<Long> implements ToBytesAware<Long> {

    @Override
    public byte[] toBytes(Long bean, StringBuilder printer) {
        if (printer != null) {
            printer.append(bean);
        }
        return RByte.toBytes(bean);
    }

}
