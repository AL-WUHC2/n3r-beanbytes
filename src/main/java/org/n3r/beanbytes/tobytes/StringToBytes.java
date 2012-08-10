package org.n3r.beanbytes.tobytes;

import org.n3r.beanbytes.BytesConverterAware;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.converter.JCStringVarLenConverter;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang.RHex;

@JCBindType(String.class)
public class StringToBytes extends BaseBytes<String> implements ToBytesAware<String> {
    @Override
    public byte[] toBytes(String str, StringBuilder printer) {
        byte[] bytes = RByte.toBytes(str, "" + getOption("charsetName", "UTF-8"));

        BytesConverterAware strConverter = converter != null ? converter : new JCStringVarLenConverter();
        bytes = strConverter.encode(bytes, str);

        if (printer != null) printer.append(RHex.encode(bytes));

        return bytes;
    }
}
