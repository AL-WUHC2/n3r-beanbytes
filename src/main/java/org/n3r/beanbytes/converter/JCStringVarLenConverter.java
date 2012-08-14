package org.n3r.beanbytes.converter;

import org.n3r.beanbytes.TypeConverter;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.impl.ByteBean;
import org.n3r.beanbytes.utils.BeanBytesUtils;

import static org.n3r.core.lang.RByte.*;

@JCApplyTo(value = String.class, linked = JCVarLen.class)
public class JCStringVarLenConverter extends TypeConverter<String> {

    @Override
    public byte[] encode(byte[] bytes, String value) {
        return BeanBytesUtils.prependLen(bytes, getLenBytesNum(), bytes.length);
    }

    /**
     * @return 第一个返回值为截取后的不带长度的字节数组， 第二个为长度占的字节数，第三个转换为字节数组时记录的长度
     */
    @Override
    public ByteBean decode(byte[] bytes, int offset) {
        ByteBean byteBean = new ByteBean();
        int lenBytesNum = getLenBytesNum();

        byte[] lenByte = subBytes(bytes, offset, lenBytesNum);
        int len = toInt(padHead(lenByte, 4));
        byteBean.setResult(subBytes(bytes, offset + lenBytesNum, len));
        byteBean.setProcessBytesNum(len + lenBytesNum);

        return byteBean;
    }

    private int getLenBytesNum() {
        JCVarLen jcLen = (JCVarLen) getAnnotation(JCVarLen.class);
        int lenBytesNum = jcLen != null ? jcLen.value() : 1;
        return lenBytesNum;
    }
}
