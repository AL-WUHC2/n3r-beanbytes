package com.ailk.phw.utils;

import org.junit.Test;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RBaseBean;
import org.n3r.core.lang.RHex;

import com.ailk.phw.utils.beans.Bean2;

import static org.junit.Assert.*;

import static org.n3r.core.lang.RByte.*;

public class Bean1Test {
    public static class Bean1 extends RBaseBean {
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
        bean1.setName("9FB1");
        Bean2 bean2 = new Bean2();
        bean2.setAge(32);
        bean1.setBean2(bean2);

        StringBuilder printer = new StringBuilder();
        byte[] bytes = new BeanToBytes<Bean1>().toBytes(bean1, printer);
        byte[] expected = BeanBytesUtils.prependLen(RHex.decode("9FB1"), 1);
        expected = add(expected, toBytes(32));

        assertArrayEquals(expected, bytes);
        assertEquals("{name:9FB1, bean2:{age:32}}", printer.toString());

        FromBytesAware<Bean1> beanFromBytes2 = new BeanFromBytes<Bean1>();
        Bean1 simpleBean1 = beanFromBytes2.fromBytes(bytes, Bean1.class, 0).getBean();
        assertEquals(bean1, simpleBean1);
    }

}
