package org.n3r.beanbytes.tobytes.primitives;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;

@JCBindType(Integer.class)
public class IntegerToBytes extends BaseBytes<Integer> implements ToBytesAware<Integer> {

    @Override
    public byte[] toBytes(Integer bean, StringBuilder printer) {
        if (printer != null) {
            printer.append(bean);
        }
        return RByte.toBytes(bean);
    }

}
