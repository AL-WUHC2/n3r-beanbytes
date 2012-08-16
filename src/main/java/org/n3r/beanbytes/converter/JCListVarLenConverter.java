package org.n3r.beanbytes.converter;

import java.util.ArrayList;
import java.util.List;

import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.TypeConverter;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCVarLen;

import static org.n3r.core.lang.RByte.*;

@JCApplyTo(value = List.class, linked = JCVarLen.class)
public class JCListVarLenConverter extends TypeConverter<List<Object>> {

    @Override
    public byte[] encode(List<Object> value) {
        JCVarLen jcVarLen = (JCVarLen) getAnnotation(JCVarLen.class);
        int lenBytes = jcVarLen == null ? 1 : jcVarLen.lenBytes();
        byte[] lengthBytes = toBytes(value.size());
        return subBytes(lengthBytes, lengthBytes.length - lenBytes);
    }

    @Override
    public ParseBean<List<Object>> decode(byte[] bytes, int offset) {
        JCVarLen jcVarLen = (JCVarLen) getAnnotation(JCVarLen.class);
        int lenBytes = jcVarLen == null ? 1 : jcVarLen.lenBytes();
        byte[] lenByte = subBytes(bytes, offset, lenBytes);
        int size = toInt(padHead(lenByte, 4));

        List<Object> result = new ArrayList<Object>();
        for (int i = 0; i < size; i++) {
            result.add(null);
        }

        return new ParseBean<List<Object>>(result, lenBytes);
    }
}
