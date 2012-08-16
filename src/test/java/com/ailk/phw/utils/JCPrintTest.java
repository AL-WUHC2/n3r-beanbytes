package com.ailk.phw.utils;

import org.junit.Test;
import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.core.lang.RBaseBean;

import static org.junit.Assert.*;

import static org.n3r.core.lang.RByte.*;

public class JCPrintTest {
    @Test
    public void test1() {
        BeanToBytes<JCPrintBean> toBytes = new BeanToBytes<JCPrintBean>();
        JCPrintBean bean = new JCPrintBean();
        bean.setName("hjb");
        bean.setAddr("nj");
        bean.setEmail("bjh@qq.com");
        StringBuilder printer = new StringBuilder();
        byte[] bytes = toBytes.toBytes(bean, printer);
        byte[] expected = padTail(toBytes("hjb"), 10);
        expected = add(expected, padTail(toBytes("nj"), 10, (byte) 0xFF));
        expected = add(expected, padTail(toBytes("bjh@qq.com"), 20, (byte) 0x00));

        assertArrayEquals(expected, bytes);

        assertSame(JCDataType.ASCII, JCDataType.valueOf("ASCII"));

        bytes = toBytes.toBytes(bean, null);
        assertArrayEquals(expected, bytes);
    }

    public static class JCPrintBean extends RBaseBean {
        @JCFixLen(length = 10, dataType = JCDataType.Octet)
        private String name;
        @JCFixLen(length = 10, pad = "FF", dataType = JCDataType.Octet)
        private String addr;
        @JCFixLen(length = 20, dataType = JCDataType.Octet)
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

    }
}
