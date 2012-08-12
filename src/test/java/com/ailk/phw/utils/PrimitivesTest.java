package com.ailk.phw.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import static org.n3r.core.lang.RByte.*;

import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void testSimplePrimitive() {
        boolean bool = false;
        ToBytesAware<Boolean> beanToBytes1 = new BeanToBytes<Boolean>();
        FromBytesAware<Boolean> beanFromBytes1 = new BeanFromBytes<Boolean>();
        StringBuilder printer = new StringBuilder();
        byte[] bytes = beanToBytes1.toBytes(bool, printer);
        assertEquals("false", printer.toString());
        byte[] expected = toBytes(bool);
        assertArrayEquals(expected, bytes);
        assertEquals(bool, beanFromBytes1.fromBytes(bytes, Boolean.class, 0).getBean());
        printer.delete(0, printer.length());

        byte bt = (byte) 0x12;
        ToBytesAware<Byte> beanToBytes2 = new BeanToBytes<Byte>();
        FromBytesAware<Byte> beanFromBytes2 = new BeanFromBytes<Byte>();
        bytes = beanToBytes2.toBytes(bt, printer);
        assertEquals("" + bt, printer.toString());
        expected = toBytes(bt);
        assertArrayEquals(expected, bytes);
        assertEquals(bt, beanFromBytes2.fromBytes(bytes, Byte.class, 0).getBean().byteValue());
        printer.delete(0, printer.length());

        int i = 10;
        ToBytesAware<Integer> beanToBytes3 = new BeanToBytes<Integer>();
        FromBytesAware<Integer> beanFromBytes3 = new BeanFromBytes<Integer>();
        bytes = beanToBytes3.toBytes(i, printer);
        assertEquals("" + i, printer.toString());
        expected = toBytes(i);
        assertArrayEquals(expected, bytes);
        assertEquals(i, beanFromBytes3.fromBytes(bytes, Integer.class, 0).getBean().intValue());
        printer.delete(0, printer.length());

        long l = 321;
        ToBytesAware<Long> beanToBytes4 = new BeanToBytes<Long>();
        FromBytesAware<Long> beanFromBytes4 = new BeanFromBytes<Long>();
        bytes = beanToBytes4.toBytes(l, printer);
        assertEquals("" + l, printer.toString());
        expected = toBytes(l);
        assertArrayEquals(expected, bytes);
        assertEquals(l, beanFromBytes4.fromBytes(beanToBytes4.toBytes(l, null), Long.class, 0).getBean().longValue());
        printer.delete(0, printer.length());

        short s = 123;
        ToBytesAware<Short> beanToBytes5 = new BeanToBytes<Short>();
        FromBytesAware<Short> beanFromBytes5 = new BeanFromBytes<Short>();
        bytes = beanToBytes5.toBytes(s, printer);
        assertEquals("" + s, printer.toString());
        expected = toBytes(s);
        assertArrayEquals(expected, bytes);
        assertEquals(s, beanFromBytes5.fromBytes(beanToBytes5.toBytes(s, null), Short.class, 0).getBean().shortValue());

    }

    @Test
    public void testPrimitiveBean() {
        PrimitiveBean bean = new PrimitiveBean();
        bean.setBool(false);
        bean.setBt((byte) 0x01);
        bean.setI(12);
        bean.setL(23L);
        bean.setS((short) 34);
        BeanToBytes<PrimitiveBean> toBytes = new BeanToBytes<PrimitiveBean>();
        StringBuilder printer = new StringBuilder();
        byte[] bytes = toBytes.toBytes(bean, printer);
        byte[] expecteds = add(toBytes(false), toBytes((byte) 0x01), toBytes(12));
        expecteds = add(expecteds, toBytes(23L), toBytes((short) 34));
        assertArrayEquals(expecteds, bytes);
        assertEquals("{bool:false, bt:1, i:12, l:23, s:34}", printer.toString());

        BeanFromBytes<PrimitiveBean> fromBytes = new BeanFromBytes<PrimitiveBean>();
        assertEquals(bean, fromBytes.fromBytes(bytes, PrimitiveBean.class, 0).getBean());

    }

    public static class PrimitiveBean {
        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        private boolean bool;
        private byte bt;
        private int i;
        private long l;
        private short s;

        public boolean isBool() {
            return bool;
        }

        public void setBool(boolean bool) {
            this.bool = bool;
        }

        public byte getBt() {
            return bt;
        }

        public void setBt(byte bt) {
            this.bt = bt;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public long getL() {
            return l;
        }

        public void setL(long l) {
            this.l = l;
        }

        public short getS() {
            return s;
        }

        public void setS(short s) {
            this.s = s;
        }
    }
}
