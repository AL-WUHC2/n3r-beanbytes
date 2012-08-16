package com.ailk.phw.utils;

import org.junit.Test;
import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RBaseBean;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang.RStr;

import static org.junit.Assert.*;

public class BeanBytesTest {

    @Test
    public void testHex() {
        HexBean bean = new HexBean();
        bean.setVarLenStr("141414");
        bean.setFixLenStr("14141");
        bean.setVarLenStrWithCharset("接口平台");

        StringBuilder printer = new StringBuilder();
        BeanToBytes<HexBean> beanToBytes = new BeanToBytes<HexBean>();
        byte[] bytes = beanToBytes.toBytes(bean, printer);
        byte[] expected = RByte.add(RByte.toBytes((byte) 20), RByte.toBytes((byte) 20), RByte.toBytes((byte) 20));
        byte[] fixBytes = RByte.add(RByte.toBytes((byte) 20), RByte.toBytes((byte) 20), RByte.toBytes((byte) 31));
        expected = RByte.add(BeanBytesUtils.prependLen(expected, 1), fixBytes, RByte.repeat((byte) 0xFF, 7));
        expected = RByte.add(expected, BeanBytesUtils.prependLen(RByte.toBytes("接口平台", "UTF-16LE"), 1));

        assertArrayEquals(expected, bytes);
        assertEquals("{varLenStr:141414, fixLenStr:14141, varLenStrWithCharset:接口平台}", RStr.toStr(printer));

        BeanFromBytes<HexBean> beanFromBytes = new BeanFromBytes<HexBean>();
        ParseBean<HexBean> fromBytes = beanFromBytes.fromBytes(bytes, HexBean.class, 0);
        bean.setFixLenStr("14141F");
        assertEquals(bean, fromBytes.getBean());
    }

    @Test
    public void testAscii() {
        AsciiBean bean = new AsciiBean();
        bean.setVarLenStr("test1");
        bean.setFixLenStr("test2");
        bean.setVarLenStrWithCharset("接口平台");

        StringBuilder printer = new StringBuilder();
        BeanToBytes<AsciiBean> beanToBytes = new BeanToBytes<AsciiBean>();
        byte[] bytes = beanToBytes.toBytes(bean, printer);
        byte[] expected = BeanBytesUtils.prependLen(RByte.toBytes("test1", "UTF-8"), 2);
        expected = RByte.add(expected, RByte.toBytes("test2", "UTF-8"), RByte.repeat(5));
        expected = RByte.add(expected, BeanBytesUtils.prependLen(RByte.toBytes("接口平台", "UTF-16LE"), 1));

        assertArrayEquals(expected, bytes);
        assertEquals("{varLenStr:test1, fixLenStr:test2, varLenStrWithCharset:接口平台}", RStr.toStr(printer));

        BeanFromBytes<AsciiBean> beanFromBytes = new BeanFromBytes<AsciiBean>();
        ParseBean<AsciiBean> fromBytes = beanFromBytes.fromBytes(bytes, AsciiBean.class, 0);
        assertEquals(bean, fromBytes.getBean());
    }

    @Test
    public void testOctet() {
        OctetBean bean = new OctetBean();
        bean.setVarLenStr("test");
        bean.setFixLenStr("test");
        bean.setVarLenStrWithCharset("接口平台");

        StringBuilder printer = new StringBuilder();
        BeanToBytes<OctetBean> beanToBytes = new BeanToBytes<OctetBean>();
        byte[] bytes = beanToBytes.toBytes(bean, printer);
        byte[] expected = BeanBytesUtils.prependLen(RByte.toBytes("test", "UTF-8"), 1);
        expected = RByte.add(expected, RByte.toBytes("test", "UTF-8"), RByte.repeat((byte) -1, 6));
        expected = RByte.add(expected, BeanBytesUtils.prependLen(RByte.toBytes("接口平台", "UTF-16LE"), 1));

        assertArrayEquals(expected, bytes);
        assertEquals("{varLenStr:test, fixLenStr:test, varLenStrWithCharset:接口平台}", RStr.toStr(printer));

        BeanFromBytes<OctetBean> beanFromBytes = new BeanFromBytes<OctetBean>();
        ParseBean<OctetBean> fromBytes = beanFromBytes.fromBytes(bytes, OctetBean.class, 0);
        assertEquals(bean, fromBytes.getBean());
    }

    public static class OctetBean extends RBaseBean {
        @JCVarLen(dataType = JCDataType.Octet)
        private String varLenStr;
        @JCFixLen(dataType = JCDataType.Octet, length = 10, pad = "FF")
        private String fixLenStr;
        @JCVarLen(dataType = JCDataType.Octet, charset = "UTF-16LE")
        private String varLenStrWithCharset;

        public void setVarLenStr(String varLenStr) {
            this.varLenStr = varLenStr;
        }

        public String getVarLenStr() {
            return varLenStr;
        }

        public void setFixLenStr(String fixLenStr) {
            this.fixLenStr = fixLenStr;
        }

        public String getFixLenStr() {
            return fixLenStr;
        }

        public void setVarLenStrWithCharset(String varLenStrWithCharset) {
            this.varLenStrWithCharset = varLenStrWithCharset;
        }

        public String getVarLenStrWithCharset() {
            return varLenStrWithCharset;
        }
    }

    public static class AsciiBean extends RBaseBean {
        @JCVarLen(lenBytes = 2, dataType = JCDataType.ASCII)
        private String varLenStr;
        @JCFixLen(length = 10, dataType = JCDataType.ASCII)
        private String fixLenStr;
        @JCVarLen(charset = "UTF-16LE", dataType = JCDataType.ASCII)
        private String varLenStrWithCharset;

        public void setVarLenStr(String varLenStr) {
            this.varLenStr = varLenStr;
        }

        public String getVarLenStr() {
            return varLenStr;
        }

        public void setFixLenStr(String fixLenStr) {
            this.fixLenStr = fixLenStr;
        }

        public String getFixLenStr() {
            return fixLenStr;
        }

        public void setVarLenStrWithCharset(String varLenStrWithCharset) {
            this.varLenStrWithCharset = varLenStrWithCharset;
        }

        public String getVarLenStrWithCharset() {
            return varLenStrWithCharset;
        }
    }

    public static class HexBean extends RBaseBean {
        private String varLenStr;
        @JCFixLen(length = 10)
        private String fixLenStr;
        @JCVarLen(charset = "UTF-16LE")
        private String varLenStrWithCharset;

        public void setVarLenStr(String varLenStr) {
            this.varLenStr = varLenStr;
        }

        public String getVarLenStr() {
            return varLenStr;
        }

        public void setFixLenStr(String fixLenStr) {
            this.fixLenStr = fixLenStr;
        }

        public String getFixLenStr() {
            return fixLenStr;
        }

        public void setVarLenStrWithCharset(String varLenStrWithCharset) {
            this.varLenStrWithCharset = varLenStrWithCharset;
        }

        public String getVarLenStrWithCharset() {
            return varLenStrWithCharset;
        }
    }

}
