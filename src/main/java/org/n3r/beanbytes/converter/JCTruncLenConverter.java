package org.n3r.beanbytes.converter;

import org.n3r.beanbytes.TypeConverter;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCTruncLen;
import org.n3r.beanbytes.impl.ByteBean;
import org.n3r.core.lang.RByte;

@JCApplyTo(value = Integer.class, linked = JCTruncLen.class)
public class JCTruncLenConverter<T> extends TypeConverter<T> {

    @Override
    public byte[] encode(byte[] bytes, T value) {
        JCTruncLen jcTruncLen = (JCTruncLen) getAnnotation(JCTruncLen.class);

        return RByte.subBytes(bytes, getTruncLen(jcTruncLen));
    }

    @Override
    public ByteBean decode(byte[] bytes, int offset) {
        JCTruncLen jcTruncLen = (JCTruncLen) getAnnotation(JCTruncLen.class);
        ByteBean result = new ByteBean();

        int truncLen = getTruncLen(jcTruncLen);
        result.setResult(RByte.insertBytes(bytes, offset, truncLen, getTruncPad(jcTruncLen)));
        result.setProcessBytesNum(-truncLen);

        return result;
    }

    private int getTruncLen(JCTruncLen jcTruncLen) {
        return jcTruncLen != null ? jcTruncLen.value() : 0;
    }

    private byte getTruncPad(JCTruncLen jcTruncLen) {
        return RByte.parseBytes(jcTruncLen != null ? jcTruncLen.pad() : "00")[0];
    }
}
