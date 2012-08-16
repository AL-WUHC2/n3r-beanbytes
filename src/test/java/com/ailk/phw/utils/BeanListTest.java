package com.ailk.phw.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCOption;
import org.n3r.beanbytes.annotations.JCOptions;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RBaseBean;
import org.n3r.core.lang.RByte;

import com.ailk.phw.utils.beans.Bean2;
import com.google.common.collect.Lists;

import static org.junit.Assert.*;

import static org.n3r.core.lang.RByte.*;

public class BeanListTest {
    public static class Bean1 extends RBaseBean {
        @JCVarLen(dataType = JCDataType.ASCII)
        private String name;
        private List<Bean2> bean2;
        @JCOptions( { @JCOption(name = "ItemDataType", value = "ASCII") })
        @JCVarLen(lenBytes = 1)
        private List<String> strLs;
        @JCOptions( {
                @JCOption(name = "ItemLen", value = "org.n3r.beanbytes.annotations.JCFixLen"),
                @JCOption(name = "ItemLength", value = "10"),
                @JCOption(name = "ItemDataType", value = "ASCII") })
        @JCVarLen(lenBytes = 1)
        private List<String> strLs2;

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

        public void setStrLs(List<String> strLs) {
            this.strLs = strLs;
        }

        public List<String> getStrLs() {
            return strLs;
        }

        public void setStrLs2(List<String> strLs2) {
            this.strLs2 = strLs2;
        }

        public List<String> getStrLs2() {
            return strLs2;
        }
    }

    @Test
    public void test1() {
        Bean1 bean1 = new Bean1();
        bean1.setName("hjb");
        Bean2 bean2 = new Bean2();
        bean2.setAge(32);
        bean1.setBean2(Arrays.asList(bean2));
        bean1.setStrLs(Arrays.asList("123", "456"));
        bean1.setStrLs2(Arrays.asList("abc", "def"));

        StringBuilder printer = new StringBuilder();
        byte[] bytes = new BeanToBytes<Bean1>().toBytes(bean1, printer);
        byte[] expected = BeanBytesUtils.prependLen(toBytes("hjb"), 1);
        expected = add(expected, BeanBytesUtils.prependLen(toBytes(32), 1, 1));
        byte[] strLsBytes = RByte.add(BeanBytesUtils.prependLen(toBytes("123"), 1),
                BeanBytesUtils.prependLen(toBytes("456"), 1));
        expected = add(expected, BeanBytesUtils.prependLen(strLsBytes, 1, 2));
        byte[] strLsBytes2 = RByte.add(toBytes("abc"), repeat(7), toBytes("def"), repeat(7));
        expected = add(expected, BeanBytesUtils.prependLen(strLsBytes2, 1, 2));

        assertArrayEquals(expected, bytes);
        assertEquals("{name:hjb, bean2:[{age:32}], strLs:[123, 456], strLs2:[abc, def]}", printer.toString());

        FromBytesAware<Bean1> beanFromBytes2 = new BeanFromBytes<Bean1>();
        Bean1 simpleBean1 = beanFromBytes2.fromBytes(bytes, Bean1.class, 0).getBean();
        assertEquals(bean1, simpleBean1);
    }

    @Test
    public void testNull() {
        Bean1 bean1 = new Bean1();
        bean1.setName("hjb");
        bean1.setBean2(Arrays.asList(new Bean2()));
        bean1.setStrLs(Arrays.asList("123"));

        ToBytesAware<Bean1> toBytes1 = new BeanToBytes<Bean1>();
        FromBytesAware<Bean1> fromBytes1 = new BeanFromBytes<Bean1>();
        byte[] bytes = toBytes1.toBytes(bean1, null);
        byte[] expected = BeanBytesUtils.prependLen(toBytes("hjb"), 1);
        expected = add(expected, BeanBytesUtils.prependLen(toBytes(0), 1, 1));
        expected = add(expected, BeanBytesUtils.prependLen(BeanBytesUtils.prependLen(toBytes("123"), 1), 1, 1));
        assertArrayEquals(expected, bytes);
        Bean1 bean2 = fromBytes1.fromBytes(bytes, Bean1.class, 0).getBean();
        assertEquals(bean1, bean2);
    }

    @Test
    public void testEmpty() {
        Bean1 bean1 = new Bean1();
        bean1.setName("hjb");
        bean1.setBean2(new ArrayList<Bean2>());
        bean1.setStrLs(new ArrayList<String>());

        StringBuilder printer = new StringBuilder();
        byte[] bytes = new BeanToBytes<Bean1>().toBytes(bean1, printer);
        byte[] expected = BeanBytesUtils.prependLen(toBytes("hjb"), 1);
        expected = add(expected, toBytes((byte) 0), toBytes((byte) 0));

        assertArrayEquals(expected, bytes);
        assertEquals("{name:hjb, bean2:[], strLs:[], strLs2:}", printer.toString());

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
        bean1.setStrLs(new ArrayList<String>());

        StringBuilder printer = new StringBuilder();
        byte[] bytes = new BeanToBytes<Bean1>().toBytes(bean1, printer);
        byte[] expected = BeanBytesUtils.prependLen(toBytes("hjb"), 1);

        expected = add(expected, toBytes((byte) 3));
        expected = add(expected, toBytes(32));
        expected = add(expected, toBytes(45));
        expected = add(expected, toBytes(68));
        expected = add(expected, toBytes((byte) 0));

        assertArrayEquals(expected, bytes);
        assertEquals("{name:hjb, bean2:[{age:32}, {age:45}, {age:68}], strLs:[], strLs2:}", printer.toString());

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
        catch (Exception e) {
            assertEquals("Unkown List Item Class for field list", e.getMessage());
        }
    }

    public static class ExcepBean extends RBaseBean {

        @JCVarLen(dataType = JCDataType.ASCII)
        private String name;

        @JCVarLen(lenBytes = 1)
        private List<?> list;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setList(List<?> list) {
            this.list = list;
        }

        public List<?> getList() {
            return list;
        }
    }

}
