package org.n3r.beanbytes.frombytes;

import org.n3r.beanbytes.BytesConverterAware;
import org.n3r.beanbytes.FromByteBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.converter.JCStringVarLenConverter;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.beanbytes.impl.ByteBean;
import org.n3r.core.lang.RByte;

@JCBindType(String.class)
public class StringFromBytes extends BaseBytes<String> implements FromBytesAware<String> {

    @Override
    public FromByteBean<String> fromBytes(byte[] bytes, Class<?> class1, int offset) {
        BytesConverterAware strConverter = converter != null ? converter : new JCStringVarLenConverter();
        ByteBean byteBean = strConverter.decode(bytes, offset);
        byte[] bytes2 = byteBean.getResult();

        FromByteBean<String> result = new FromByteBean<String>();
        result.setBean(RByte.toString(bytes2, 0, bytes2.length, "" + getOption("charsetName", "UTF-8")));
        result.setBytesSize(byteBean.getProcessBytesNum());
        return result;
    }
}
