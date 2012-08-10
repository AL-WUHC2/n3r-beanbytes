package com.ailk.phw.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.junit.Test;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RByte;

import com.google.common.collect.Lists;

import static org.junit.Assert.*;

public class BeanListTest {
    public static class Bean2 {

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        private int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static class Bean1 {

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        private String name;
        @JCVarLen(1)
        private List<Bean2> bean2;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Bean2> getBean2() {
            return bean2;
        }

        public void setBean2(List<Bean2> bean2) {
            this.bean2 = bean2;
        }
    }

    @Test
    public void test1() {
        Bean1 bean1 = new Bean1();
        bean1.setName("hjb");
        Bean2 bean2 = new Bean2();
        bean2.setAge(32);
        bean1.setBean2(Arrays.asList(bean2));

        StringBuilder printer = new StringBuilder();
        byte[] bytes = new BeanToBytes<Bean1>().toBytes(bean1, printer);
        byte[] expected = BeanBytesUtils.prependLen(RByte.toBytes("hjb"), 1);
        expected = RByte.add(expected, BeanBytesUtils.prependLen(RByte.toBytes(32), 1, 1));

        assertArrayEquals(expected, bytes);
        assertEquals("{name:03686A62, bean2:[{age:32}]}", printer.toString());

        FromBytesAware<Bean1> beanFromBytes2 = new BeanFromBytes<Bean1>();
        Bean1 simpleBean1 = beanFromBytes2.fromBytes(bytes, Bean1.class, 0).getBean();
        assertEquals(bean1, simpleBean1);
    }

    @Test
    public void testNull() {
        Bean1 bean1 = new Bean1();
        bean1.setName("hjb");
        bean1.setBean2(null);

        ToBytesAware<Bean1> toBytes1 = new BeanToBytes<Bean1>();
        FromBytesAware<Bean1> fromBytes1 = new BeanFromBytes<Bean1>();
        byte[] bytes = toBytes1.toBytes(bean1, null);
        byte[] expected = BeanBytesUtils.prependLen(RByte.toBytes("hjb"), 1);
        assertArrayEquals(expected, bytes);
        Bean1 bean2 = fromBytes1.fromBytes(bytes, Bean1.class, 0).getBean();
        assertEquals(bean1, bean2);
    }

    @Test
    public void testEmpty() {
        Bean1 bean1 = new Bean1();
        bean1.setName("hjb");
        bean1.setBean2(new ArrayList());

        StringBuilder printer = new StringBuilder();
        byte[] bytes = new BeanToBytes<Bean1>().toBytes(bean1, printer);
        byte[] expected = BeanBytesUtils.prependLen(RByte.toBytes("hjb"), 1);
        expected = RByte.add(expected, RByte.toBytes((byte) 0));

        assertArrayEquals(expected, bytes);
        assertEquals("{name:03686A62, bean2:[]}", printer.toString());

        FromBytesAware<Bean1> beanFromBytes2 = new BeanFromBytes<Bean1>();
        Bean1 simpleBean1 = beanFromBytes2.fromBytes(bytes, Bean1.class, 0).getBean();
        assertEquals(bean1, simpleBean1);
    }

    @Test
    public void testList() {
        Bean1 bean1 = new Bean1();
        bean1.setName("hjb");
        Bean2 bean21 = new Bean2();
        bean21.setAge(32);
        Bean2 bean22 = new Bean2();
        bean22.setAge(45);
        Bean2 bean23 = new Bean2();
        bean23.setAge(68);
        bean1.setBean2(Arrays.asList(bean21, bean22, bean23));

        StringBuilder printer = new StringBuilder();
        byte[] bytes = new BeanToBytes<Bean1>().toBytes(bean1, printer);
        byte[] expected = BeanBytesUtils.prependLen(RByte.toBytes("hjb"), 1);

        expected = RByte.add(expected, RByte.toBytes((byte) 3));
        expected = RByte.add(expected, RByte.toBytes(32));
        expected = RByte.add(expected, RByte.toBytes(45));
        expected = RByte.add(expected, RByte.toBytes(68));

        assertArrayEquals(expected, bytes);
        assertEquals("{name:03686A62, bean2:[{age:32}, {age:45}, {age:68}]}", printer.toString());

        FromBytesAware<Bean1> beanFromBytes2 = new BeanFromBytes<Bean1>();
        Bean1 simpleBean1 = beanFromBytes2.fromBytes(bytes, Bean1.class, 0).getBean();
        assertEquals(bean1, simpleBean1);
    }

    @Test
    public void testExcepList() {
        ExcepBean bean = new ExcepBean();
        bean.setName("hjb");
        bean.setList(Lists.newArrayList());

        BeanToBytes<ExcepBean> toBytes = new BeanToBytes<ExcepBean>();
        byte[] bytes = toBytes.toBytes(bean, null);
        BeanFromBytes<ExcepBean> fromBytes = new BeanFromBytes<ExcepBean>();

        try {
            fromBytes.fromBytes(bytes, ExcepBean.class, 0);
            fail();
        }
        catch (RuntimeException e) {
            assertEquals("Unkown List Item Class for field list", e.getMessage());
        }
    }

    public static class ExcepBean {

        private String name;
        @JCVarLen(1)
        private List list;

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setList(List list) {
            this.list = list;
        }

        public List getList() {
            return list;
        }
    }

}
