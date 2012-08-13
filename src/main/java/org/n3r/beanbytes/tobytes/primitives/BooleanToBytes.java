package org.n3r.beanbytes.tobytes.primitives;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang.RStr;

@JCBindType(Boolean.class)
public class BooleanToBytes extends BaseBytes<Boolean> implements ToBytesAware<Boolean> {

    @Override
    public byte[] toBytes(Boolean bean, StringBuilder printer) {
        RStr.append(printer, bean);

        return RByte.toBytes(bean);
    }

}
