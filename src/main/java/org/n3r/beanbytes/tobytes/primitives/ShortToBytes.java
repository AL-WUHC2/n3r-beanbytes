package org.n3r.beanbytes.tobytes.primitives;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang.RStr;

@JCBindType(Short.class)
public class ShortToBytes extends BaseBytes<Short> implements ToBytesAware<Short> {

    @Override
    public byte[] toBytes(Short bean, StringBuilder printer) {
        RStr.append(printer, bean);

        return RByte.toBytes(bean);
    }

}
