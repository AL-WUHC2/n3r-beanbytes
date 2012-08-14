package org.n3r.beanbytes.converter;

import java.util.List;

import org.n3r.beanbytes.TypeConverter;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.impl.ByteBean;
import org.n3r.beanbytes.utils.BeanBytesUtils;

import static org.n3r.core.lang.RByte.*;

@JCApplyTo(value = List.class, linked = JCVarLen.class)
public class JCListVarLenConverter extends TypeConverter<List<Object>> {

    @Override
    public byte[] encode(byte[] bytes, List<Object> value) {
        JCVarLen jcLen = field.getAnnotation(JCVarLen.class);

        return BeanBytesUtils.prependLen(bytes, jcLen.value(), value.size());
    }

    @Override
    public ByteBean decode(byte[] bytes, int offset) {
        JCVarLen jcLen = field.getAnnotation(JCVarLen.class);
        byte[] lenByte = subBytes(bytes, offset, jcLen.value());
        int size = toInt(padHead(lenByte, 4));

        ByteBean byteBean = new ByteBean();
        byteBean.setResult(subBytes(bytes, offset + jcLen.value()));
        byteBean.setProcessBytesNum(jcLen.value());
        byteBean.setSize(size);

        return byteBean;
    }

}
