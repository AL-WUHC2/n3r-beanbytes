package org.n3r.beanbytes.converter;

import org.apache.commons.lang3.tuple.Pair;
import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCPairFixLen;
import org.n3r.beanbytes.utils.JCDataTypeUtils;

import static org.apache.commons.lang3.StringUtils.*;
import static org.n3r.core.lang.RByte.*;

@JCApplyTo(value = Pair.class, linked = JCPairFixLen.class)
public class JCPairFixLenConverter extends JCPairConverter<String, String> {

    private JCDataType kdataType;
    private int klength;
    private String kpad;
    private String kcharset;

    private JCDataType vdataType;
    private int vlength;
    private String vpad;
    private String vcharset;

    @Override
    public void setVParams(Object... params) {
        vlength = (Integer) params[0];
    }

    @Override
    public byte[] encode(Pair<String, String> value, StringBuilder printer) {
        setConvertParam();

        byte[] kbytes = JCDataTypeUtils.encode(value.getKey(), kdataType, kcharset);
        kbytes = padTail(kbytes, klength, getKPadChar());

        doProcess(value.getKey());

        byte[] vbytes = JCDataTypeUtils.encode(value.getValue(), vdataType, vcharset);
        vbytes = padTail(vbytes, vlength, getVPadChar());

        return add(kbytes, vbytes);
    }

    @Override
    public ParseBean<Pair<String, String>> decode(byte[] bytes, int offset) {
        setConvertParam();

        byte[] parseKBytes = trimTail(subBytes(bytes, offset, klength), getKPadChar());
        String key = JCDataTypeUtils.decode(parseKBytes, kdataType, kcharset);

        doProcess(key);

        byte[] parseVBytes = trimTail(subBytes(bytes, offset + klength, vlength), getVPadChar());
        String value = JCDataTypeUtils.decode(parseVBytes, vdataType, vcharset);

        return new ParseBean<Pair<String, String>>(Pair.of(key, value), klength + vlength);
    }

    protected byte getKPadChar() {
        return getPadChar(kpad, kdataType);
    }

    protected byte getVPadChar() {
        return getPadChar(vpad, vdataType);
    }

    protected byte getPadChar(String pad, JCDataType dataType) {
        return !isEmpty(pad) ? parseBytes(pad)[0] : JCDataTypeUtils.getDefaultPadByte(dataType);
    }

    public void setConvertParam() {
        setConvertParam(JCDataType.HEX, 1, "", "", JCDataType.HEX, 0, "", "", JCFixKeyPairProcess.class);
    }

    public void setConvertParam(JCDataType kdataType, int klength, String kpad, String kcharset,
                JCDataType vdataType, int vlength, String vpad, String vcharset,
                Class<? extends JCFixKeyPairProcess> processer) {
        JCPairFixLen jcPairFixLen = (JCPairFixLen) getAnnotation(JCPairFixLen.class);

        this.kdataType = jcPairFixLen == null ? kdataType : jcPairFixLen.kfieldType().dataType();
        this.klength = jcPairFixLen == null ? klength : jcPairFixLen.kfieldType().length();
        this.kpad = jcPairFixLen == null ? kpad : jcPairFixLen.kfieldType().pad();
        this.kcharset = jcPairFixLen == null ? kcharset : jcPairFixLen.kfieldType().charset();

        this.vdataType = jcPairFixLen == null ? vdataType : jcPairFixLen.vdataType();
        this.vlength = jcPairFixLen == null ? vlength : jcPairFixLen.vlength();
        this.vpad = jcPairFixLen == null ? vpad : jcPairFixLen.vpad();
        this.vcharset = jcPairFixLen == null ? vcharset : jcPairFixLen.vcharset();

        setProcesser(jcPairFixLen == null ? processer : jcPairFixLen.processer());
    }
}
