package com.ailk.phw.utils;

import org.junit.Test;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCTransient;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.core.lang.RBaseBean;
import org.n3r.core.lang.RByte;

import static org.junit.Assert.*;

import static org.n3r.beanbytes.utils.BeanBytesUtils.*;
import static org.n3r.core.lang.RByte.*;

public class SimpleBean1Test {

    @Test
    public void test1() {
        ToBytesAware<String> beanToBytes = new BeanToBytes<String>();
        byte[] bytes = beanToBytes.toBytes("0x1234", null);
        byte[] expected = RByte.add(RByte.toBytes((byte) 18), RByte.toBytes((byte) 52));
        assertArrayEquals(prependLen(expected, 1), bytes);

        FromBytesAware<String> beanFromBytes = new BeanFromBytes<String>();
        beanFromBytes.addOption("charsetName", "UTF-16LE");
        String str = beanFromBytes.fromBytes(bytes, String.class, 0).getBean();
        assertEquals("1234", str);

        ToBytesAware<Integer> beanToBytes2 = new BeanToBytes<Integer>();
        bytes = beanToBytes2.toBytes(123, null);
        expected = toBytes(123);
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

        byte[] expected = toBytes("wuhaocheng");
        expected = prependLen(expected, 1);

        assertArrayEquals(add(expected, toBytes(24)), bytes);

        FromBytesAware<SimpleBean1> beanFromBytes = new BeanFromBytes<SimpleBean1>();
        SimpleBean1 bean = beanFromBytes.fromBytes(bytes, SimpleBean1.class, 0).getBean();

        assertEquals(simpleBean, bean);
    }

    public static class SimpleBean1 extends RBaseBean {
        @JCVarLen(dataType = JCDataType.ASCII)
        private String name;
        private int age;
        @JCTransient
        private String address;

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
