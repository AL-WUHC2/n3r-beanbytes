package com.ailk.phw.utils;

import org.junit.Test;
import org.n3r.beanbytes.FromByteBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCTruncLen;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RBaseBean;
import org.n3r.core.lang.RByte;

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

    public static class TruncBean extends RBaseBean {
        private int noTrunc;
        @JCTruncLen(value = 1, pad = "01")
        private int trunc;
        private int noTrunc2;

        public void setNoTrunc(int noTrunc) {
            this.noTrunc = noTrunc;
        }

        public int getNoTrunc() {
            return noTrunc;
        }

        public void setTrunc(int trunc) {
            this.trunc = trunc;
        }

        public int getTrunc() {
            return trunc;
        }

        public void setNoTrunc2(int noTrunc2) {
            this.noTrunc2 = noTrunc2;
        }

        public int getNoTrunc2() {
            return noTrunc2;
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
        byte[] expected = BeanBytesUtils.prependLen(toBytes("hjb"), 1);
        expected = add(expected, toBytes(32));

        assertArrayEquals(expected, bytes);
        assertEquals("{name:03686A62, bean2:{age:32}}", printer.toString());

        FromBytesAware<Bean1> beanFromBytes2 = new BeanFromBytes<Bean1>();
        Bean1 simpleBean1 = beanFromBytes2.fromBytes(bytes, Bean1.class, 0).getBean();
        assertEquals(bean1, simpleBean1);
    }

    @Test
    public void test2() {
        TruncBean bean = new TruncBean();
        bean.setNoTrunc(16777216);
        bean.setTrunc(16777216);
        bean.setNoTrunc2(16777216);
        byte[] bytes = new BeanToBytes<TruncBean>().toBytes(bean, null);
        byte[] expected = RByte.add(RByte.toBytes(16777216), RByte.toBytes((byte) 0), RByte.toBytes((short) 0));
        expected = RByte.add(expected, RByte.toBytes(16777216));
        assertArrayEquals(expected, bytes);

        FromByteBean<TruncBean> fromBytes = new BeanFromBytes<TruncBean>().fromBytes(bytes, TruncBean.class, 0);
        assertEquals(bean, fromBytes.getBean());
    }
}
