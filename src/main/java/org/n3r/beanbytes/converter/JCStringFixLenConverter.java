package org.n3r.beanbytes.converter;

import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.TypeConverter;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.utils.JCDataTypeUtils;

import static org.n3r.core.lang.RByte.*;
import static org.n3r.core.lang3.StringUtils.*;

@JCApplyTo(value = String.class, linked = JCFixLen.class)
public class JCStringFixLenConverter extends TypeConverter<String> {

    private JCDataType dataType;
    private int length;
    private String pad;
    private String charset;

    @Override
    public byte[] encode(String value) {
        if (dataType == null) setConvertParam();
        byte[] bytes = JCDataTypeUtils.encode(value, dataType, charset);
        return padTail(bytes, length, getPadChar(pad));
    }

    @Override
    public ParseBean<String> decode(byte[] bytes, int offset) {
        if (dataType == null) setConvertParam();
        byte[] parseBytes = trimTail(subBytes(bytes, offset, length), getPadChar(pad));

        String result = JCDataTypeUtils.decode(parseBytes, dataType, charset);
        return new ParseBean<String>(result, length);
    }

    private byte getPadChar(String pad) {
        return !isEmpty(pad) ? parseBytes(pad)[0] : JCDataTypeUtils.getDefaultPadByte(dataType);
    }

    public void setConvertParam() {
        setConvertParam(JCDataType.HEX, 0, "", "");
    }

    public void setConvertParam(JCDataType dataType, int length, String pad, String charset) {
        JCFixLen jcFixLen = (JCFixLen) getAnnotation(JCFixLen.class);
        this.dataType = jcFixLen == null ? dataType : jcFixLen.dataType();
        this.length = jcFixLen == null ? length : jcFixLen.length();
        this.pad = jcFixLen == null ? pad : jcFixLen.pad();
        this.charset = jcFixLen == null ? charset : jcFixLen.charset();
    }
}
