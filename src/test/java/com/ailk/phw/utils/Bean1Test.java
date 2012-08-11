package com.ailk.phw.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RByte;

import static org.junit.Assert.*;

public class Bean1Test {
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
        private Bean2 bean2;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Bean2 getBean2() {
            return bean2;
        }

        public void setBean2(Bean2 bean2) {
            this.bean2 = bean2;
        }
    }

    @Test
    public void test1() {
        Bean1 bean1 = new Bean1();
        bean1.setName("hjb");
        Bean2 bean2 = new Bean2();
        bean2.setAge(32);
        bean1.setBean2(bean2);

        StringBuilder printer = new StringBuilder();
        byte[] bytes = new BeanToBytes<Bean1>().toBytes(bean1, printer);
        byte[] expected = BeanBytesUtils.prependLen(RByte.toBytes("hjb"), 1);
        expected = RByte.add(expected, RByte.toBytes(32));

        assertArrayEquals(expected, bytes);
        assertEquals("{name:03686A62, bean2:{age:32}}", printer.toString());

        FromBytesAware<Bean1> beanFromBytes2 = new BeanFromBytes<Bean1>();
        Bean1 simpleBean1 = beanFromBytes2.fromBytes(bytes, Bean1.class, 0).getBean();
        assertEquals(bean1, simpleBean1);
    }
}
