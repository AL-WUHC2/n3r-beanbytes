package com.ailk.phw.utils;

import org.junit.Test;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.converter.JCNullConverter;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RBaseBean;

import static org.junit.Assert.*;

import static org.n3r.core.lang.RByte.*;

public class BeanNullTest {

    public static class NullBean extends RBaseBean {
        private Integer integer;
        @JCVarLen(dataType = JCDataType.ASCII)
        private String middle;

        private String string;

        public void setInteger(Integer integer) {
            this.integer = integer;
        }

        public Integer getInteger() {
            return integer;
        }

        public void setMiddle(String middle) {
            this.middle = middle;
        }

        public String getMiddle() {
            return middle;
        }

        public void setString(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }

    @Test
    public void testNull() {
        NullBean nullBean1 = new NullBean();
        nullBean1.setInteger(12);
        String middleStr = "middle";
        nullBean1.setMiddle(middleStr);
        ToBytesAware<NullBean> toBytes1 = new BeanToBytes<NullBean>();
        FromBytesAware<NullBean> fromBytes1 = new BeanFromBytes<NullBean>();
        byte[] bytes = toBytes1.toBytes(nullBean1, null);
        byte[] expected = add(toBytes(12), BeanBytesUtils.prependLen(toBytes(middleStr), 1));
        assertArrayEquals(expected, bytes);
        NullBean nullBean2 = fromBytes1.fromBytes(bytes, NullBean.class, 0).getBean();
        assertEquals(nullBean1, nullBean2);
    }

    @Test
    public void testNull2() {
        NullBean nullBean = new NullBean();
        nullBean.setInteger(12);
        nullBean.setString("string");
        BeanToBytes<NullBean> toBytes = new BeanToBytes<NullBean>();
        try {
            toBytes.toBytes(nullBean, null);
            fail();
        }
        catch (RuntimeException e) {
            assertEquals("Field middle is not allowed null.", e.getMessage());
        }
    }

    @Test
    public void testNullConverter() {
        JCNullConverter<Object> jcNullConverter = new JCNullConverter<Object>();
        byte[] bytes = jcNullConverter.encode(null, null);

        assertArrayEquals(new byte[0], bytes);
        assertNull(jcNullConverter.decode(bytes, 0).getBean());
    }
}
