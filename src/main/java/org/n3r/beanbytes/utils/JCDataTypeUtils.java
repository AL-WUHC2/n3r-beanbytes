package org.n3r.beanbytes.utils;

import org.n3r.beanbytes.JCDataType;
import org.n3r.core.lang.RHex;
import org.n3r.core.lang.RStr;

import static org.n3r.core.lang.RByte.*;

public class JCDataTypeUtils {
    public static byte[] encode(String value, JCDataType dataType, String charset) {
        switch (dataType) {
        case Octet:
        case ASCII:
            return toBytes(value, RStr.toStr(charset, JCConstantUtils.CHARSET_UTF8));
        case HEX:
        default:
            if (RStr.isNotEmpty(charset)) return toBytes(value, charset);
            if (value.length() % 2 == 1) value += "F";

            return RHex.decode(value);
        }
    }

    public static String decode(byte[] bytes, JCDataType dataType, String charset) {
        switch (dataType) {
        case Octet:
        case ASCII:
            return toStr(bytes, RStr.toStr(charset, JCConstantUtils.CHARSET_UTF8));
        case HEX:
        default:
            if (RStr.isNotEmpty(charset)) { return toStr(bytes, charset); }
            return RHex.encode(bytes);
        }
    }

    public static byte getDefaultPadByte(JCDataType dataType) {
        switch (dataType) {
        case ASCII:
        case Octet:
            return (byte) 0x00;
        case HEX:
        default:
            return (byte) 0xFF;
        }
    }
}
