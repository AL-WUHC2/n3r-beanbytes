package org.n3r.beanbytes.converter;

import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.TypeConverter;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.beanbytes.utils.JCDataTypeUtils;

import static org.n3r.core.lang.RByte.*;

@JCApplyTo(value = String.class, linked = JCVarLen.class)
public class JCStringVarLenConverter extends TypeConverter<String> {

    private JCDataType dataType;
    private int lenBytes;
    private String charset;

    @Override
    public byte[] encode(String value, StringBuilder printer) {
        if (dataType == null) setConvertParam();
        byte[] bytes = JCDataTypeUtils.encode(value, dataType, charset);
        return BeanBytesUtils.prependLen(bytes, lenBytes);
    }

    @Override
    public ParseBean<String> decode(byte[] bytes, int offset) {
        if (dataType == null) setConvertParam();
        int len = toInt(padHead(subBytes(bytes, offset, lenBytes), 4));
        byte[] parseBytes = subBytes(bytes, offset + lenBytes, len);

        String result = JCDataTypeUtils.decode(parseBytes, dataType, charset);
        return new ParseBean<String>(result, len + lenBytes);
    }

    public void setConvertParam() {
        setConvertParam(JCDataType.HEX, 1, "");
    }

    public void setConvertParam(JCDataType dataType, int lenBytes, String charset) {
        JCVarLen jcVarLen = (JCVarLen) getAnnotation(JCVarLen.class);
        this.dataType = jcVarLen == null ? dataType : jcVarLen.dataType();
        this.lenBytes = jcVarLen == null ? lenBytes : jcVarLen.lenBytes();
        this.charset = jcVarLen == null ? charset : jcVarLen.charset();
    }
}
