package com.ailk.phw.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCPrint;
import org.n3r.beanbytes.annotations.JCPrint.JCPrintType;
import org.n3r.beanbytes.impl.BeanToBytes;
import static org.n3r.core.lang.RByte.*;

import static org.junit.Assert.*;

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
        expected = add(expected, padTail(toBytes("nj"), 10, (byte) 0x00));
        expected = add(expected, padTail(toBytes("bjh@qq.com"), 20, (byte) 0xFF));

        assertArrayEquals(expected, bytes);

        assertSame(JCPrintType.ASCII, JCPrintType.valueOf("ASCII"));
    }

    public static class JCPrintBean {
        @JCFixLen(value = 10)
        private String name;
        @JCFixLen(value = 10, pad = "00")
        private String addr;
        @JCPrint(JCPrintType.ASCII)
        @JCFixLen(value = 20)
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
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
