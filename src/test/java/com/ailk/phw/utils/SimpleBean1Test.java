package com.ailk.phw.utils;

import static org.junit.Assert.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.junit.Test;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCTransient;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RByte;

public class SimpleBean1Test {

    @Test
    public void test1() {
        ToBytesAware<String> beanToBytes = new BeanToBytes<String>();
        beanToBytes.addOption("charsetName", "UTF-16LE");
        beanToBytes.setConverter(null);
        byte[] bytes = beanToBytes.toBytes("123", null);
        assertArrayEquals(BeanBytesUtils.prependLen(RByte.toBytes("123", "UTF-16LE"), 1), bytes);

        FromBytesAware<String> beanFromBytes = new BeanFromBytes<String>();
        beanFromBytes.addOption("charsetName", "UTF-16LE");
        String str = beanFromBytes.fromBytes(bytes, String.class, 0).getBean();
        assertEquals("123", str);

        ToBytesAware<Integer> beanToBytes2 = new BeanToBytes<Integer>();
        bytes = beanToBytes2.toBytes(123, null);
        byte[] expected = RByte.toBytes(123);
        assertArrayEquals(expected, bytes);

        FromBytesAware<Integer> beanFromBytes2 = new BeanFromBytes<Integer>();
        int i = beanFromBytes2.fromBytes(bytes, Integer.class, 0).getBean();
        assertEquals(123, i);
    }

    @Test
    public void test2() {
        SimpleBean1 simpleBean = new SimpleBean1();
        simpleBean.setName("wuhaocheng");
        simpleBean.setAge(24);

        ToBytesAware<SimpleBean1> beanToBytes = new BeanToBytes<SimpleBean1>();
        byte[] bytes = beanToBytes.toBytes(simpleBean, null);

        byte[] expected = RByte.toBytes("wuhaocheng");
        expected = BeanBytesUtils.prependLen(expected, 1);

        assertArrayEquals(RByte.add(expected, RByte.toBytes(24)), bytes);

        FromBytesAware<SimpleBean1> beanFromBytes = new BeanFromBytes<SimpleBean1>();
        SimpleBean1 bean = beanFromBytes.fromBytes(bytes, SimpleBean1.class, 0).getBean();

        assertEquals(simpleBean, bean);
    }

    public static class SimpleBean1 {
        private String name;
        private int age;
        @JCTransient
        private String address;

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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }
    }
}
