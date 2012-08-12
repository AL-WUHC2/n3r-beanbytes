package com.ailk.phw.utils;

import org.junit.Test;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCOption;
import org.n3r.beanbytes.annotations.JCOptions;
import org.n3r.beanbytes.annotations.JCPrint;
import org.n3r.beanbytes.annotations.JCPrint.JCPrintType;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.collection.RMap;
import org.n3r.core.lang.RBaseBean;
import org.n3r.core.lang.RHex;

import static org.junit.Assert.*;

import static org.n3r.core.lang.RByte.*;

public class SimpleBean2Test {

    @Test
    public void test1() {
        ToBytesAware<String> beanToBytes = new BeanToBytes<String>();
        beanToBytes.addOptions(RMap.of("charsetName", (Object) "UTF-16LE"));

        StringBuilder printer = new StringBuilder();
        byte[] bytes = beanToBytes.toBytes("吴昊成", printer);

        byte[] expected = toBytes("吴昊成", "UTF-16LE");
        expected = BeanBytesUtils.prependLen(expected, 1);

        assertArrayEquals(expected, bytes);
        assertEquals(RHex.encode(expected), printer.toString());

        FromBytesAware<String> beanFromBytes = new BeanFromBytes<String>();
        beanFromBytes.addOptions(RMap.of("charsetName", (Object) "UTF-16LE"));
        String str = beanFromBytes.fromBytes(bytes, String.class, 0).getBean();

        assertEquals("吴昊成", str);
    }

    @Test
    public void test2() {
        SimpleBean2 simpleBean = new SimpleBean2();
        simpleBean.setName("吴昊成"); // UTF-16LE 固定10
        simpleBean.setAge(24); // 固定4
        simpleBean.setAscii("ABCDEFG"); // 变长 7+1=8
        simpleBean.setAddress("nj"); // 变长 2*2 + 2 = 6
        simpleBean.setNumber("1234567"); // 定长10

        ToBytesAware<SimpleBean2> beanToBytes = new BeanToBytes<SimpleBean2>();
        StringBuilder printer = new StringBuilder();
        byte[] bytes = beanToBytes.toBytes(simpleBean, printer);
        byte[] nameBytes = add(toBytes("吴昊成", "UTF-16LE"), repeat((byte) 0x00, 4));
        byte[] expected = add(nameBytes, toBytes(24));
        expected = add(expected, parseBytes("07"), toBytes("ABCDEFG"));
        byte[] addrBytes = add(parseBytes("0004"), toBytes("nj", "UTF-16LE"));
        expected = add(expected, addrBytes);
        expected = add(expected, toBytes("1234567"), repeat((byte) 0xff, 3));

        assertArrayEquals(expected, bytes);

        assertEquals("{name:吴昊成, age:24, ascii:ABCDEFG, address:" + RHex.encode(addrBytes)
                + ", number:1234567}", printer.toString());

        FromBytesAware<SimpleBean2> beanFromBytes2 = new BeanFromBytes<SimpleBean2>();
        SimpleBean2 simpleBean2 = beanFromBytes2.fromBytes(bytes, SimpleBean2.class, 0).getBean();
        assertEquals(simpleBean, simpleBean2);
    }

    public static class SimpleBean2 extends RBaseBean {
        @JCOptions(@JCOption(name = "charsetName", value = "UTF-16LE"))
        @JCPrint(JCPrintType.Octet)
        @JCFixLen(10)
        private String name;
        private int age;
        @JCPrint(JCPrintType.ASCII)
        private String ascii;
        @JCOption(name = "charsetName", value = "UTF-16LE")
        @JCPrint(JCPrintType.HEX)
        @JCVarLen(2)
        private String address;

        @JCPrint(JCPrintType.ASCII)
        @JCFixLen(value = 10, pad = "FF")
        private String number;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getAscii() {
            return ascii;
        }

        public void setAscii(String ascii) {
            this.ascii = ascii;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getNumber() {
            return number;
        }
    }

}
