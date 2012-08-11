package org.n3r.beanbytes.converter;

import org.n3r.beanbytes.TypeConverter;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCPrint;
import org.n3r.beanbytes.impl.ByteBean;

import static org.n3r.core.lang.RByte.*;

import static org.apache.commons.lang3.StringUtils.*;

@JCApplyTo(value = String.class, linked = JCFixLen.class)
public class JCStringFixLenConverter extends TypeConverter {

    @Override
    public byte[] encode(byte[] bytes, Object value) {
        JCFixLen jcLen = field.getAnnotation(JCFixLen.class);
        byte padByte = getPadChar(jcLen);
        return padTail(bytes, jcLen.value(), padByte);
    }

    @Override
    public ByteBean decode(byte[] bytes, int offset) {
        ByteBean byteBean = new ByteBean();
        JCFixLen jcLen = field.getAnnotation(JCFixLen.class);
        byte padByte = getPadChar(jcLen);
        byteBean.setResult(trimTail(subBytes(bytes, offset, jcLen.value()), padByte));
        byteBean.setProcessBytesNum(jcLen.value());

        return byteBean;
    }

    private byte getPadChar(JCFixLen jcLen) {
        return !isEmpty(jcLen.pad()) ? parseBytes(jcLen.pad())[0] : getDefaultPadByte();
    }

    private byte getDefaultPadByte() {
        byte padChar = 0x00;

        JCPrint jcPrint = field.getAnnotation(JCPrint.class);
        if (jcPrint == null) return padChar;

        switch (jcPrint.value()) {
        case HEX:
        case ASCII:
            padChar = (byte) 0xff;
            break;
        case Octet:
        default:
            padChar = 0x00;
            break;
        }
        return padChar;
    }

}
