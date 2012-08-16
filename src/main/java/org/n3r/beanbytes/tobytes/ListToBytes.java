package org.n3r.beanbytes.tobytes;

import java.util.List;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.converter.JCListVarLenConverter;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang.RStr;

@JCBindType(List.class)
public class ListToBytes extends BaseBytes<List<Object>> implements ToBytesAware<List<Object>> {
    private static final String SEP = ", ";

    @Override
    public byte[] toBytes(List<Object> bean, StringBuilder printer) {
        byte[] result = new byte[0];

        RStr.append(printer, '[');
        for (Object item : bean) {
            BeanToBytes<Object> beanToBytes = new BeanToBytes<Object>();

            BeanBytesUtils.parseItemConverter(item.getClass(), beanToBytes, this);

            byte[] itemBytes = beanToBytes.toBytes(item, printer);
            result = RByte.add(result, itemBytes);

            RStr.append(printer, SEP);
        }

        RStr.removeTail(printer, SEP);
        RStr.append(printer, ']');

        return RByte.add(getConverter(JCListVarLenConverter.class).encode(bean), result);
    }
}
