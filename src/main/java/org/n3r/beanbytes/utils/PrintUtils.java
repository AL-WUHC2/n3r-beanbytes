package org.n3r.beanbytes.utils;

import org.n3r.beanbytes.annotations.JCPrint;
import org.n3r.core.lang.RHex;

public class PrintUtils {
    public static void print(StringBuilder printer, byte[] result, Object fieldValue, JCPrint jcPrint) {
        if (jcPrint == null || printer == null) return;

        switch (jcPrint.value()) {
        case HEX:
            printer.append(RHex.encode(result));
            break;
        case Octet:
        case ASCII:
            printer.append("" + fieldValue);
            break;
        }
    }

    public static byte getDefaultPadByte(JCPrint jcPrint) {
        byte padChar = 0x00;

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
