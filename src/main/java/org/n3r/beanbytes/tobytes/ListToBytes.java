package org.n3r.beanbytes.tobytes;

import java.util.List;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.core.lang.RByte;

@JCBindType(List.class)
public class ListToBytes extends BaseBytes<List<Object>> implements ToBytesAware<List<Object>> {

    @Override
    public byte[] toBytes(List<Object> bean, StringBuilder printer) {
        byte[] result = new byte[0];

        if (printer != null) printer.append('[');
        for (Object item : bean) {
            BeanToBytes<Object> beanToBytes = new BeanToBytes<Object>();
            byte[] itemBytes = beanToBytes.toBytes(item, printer);
            result = RByte.add(result, itemBytes);

            if (printer != null) printer.append(", ");
        }

        if (printer != null) {
            if (bean.size() > 0) printer.replace(printer.length() - 2, printer.length(), "]");
            else printer.append(']');
        }

        return converter.encode(result, bean);
    }

}
