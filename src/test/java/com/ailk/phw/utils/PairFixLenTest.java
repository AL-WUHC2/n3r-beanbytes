package com.ailk.phw.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCPairFixLen;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.core.lang.RBaseBean;
import org.n3r.core.lang.RStr;

import static org.junit.Assert.*;

import static org.n3r.core.lang.RByte.*;
import static org.n3r.core.lang.RHex.*;

public class PairFixLenTest {

    public static class PairBean extends RBaseBean {

        @JCPairFixLen(kfieldType = @JCFixLen(length = 1, dataType = JCDataType.Octet), vpad = "00", processer = IF5PairProcess.class)
        private Pair<String, String> pair;

        private Pair<String, String> pair2;

        public void setPair(Pair<String, String> pair) {
            this.pair = pair;
        }

        public Pair<String, String> getPair() {
            return pair;
        }

        public void setPair2(Pair<String, String> pair2) {
            this.pair2 = pair2;
        }

        public Pair<String, String> getPair2() {
            return pair2;
        }
    }

    @Test
    public void test1() {
        PairBean pairBean = new PairBean();
        pairBean.setPair(Pair.of("1", "6789ABCDEF"));
        pairBean.setPair2(Pair.of("04", "012345"));

        StringBuilder printer = new StringBuilder();
        byte[] actual = new BeanToBytes<PairBean>().toBytes(pairBean, printer);

        byte[] expected = add(toBytes("1"), decode("6789ABCDEF00"), decode("04"), decode("012345FF"));

        assertArrayEquals(actual, expected);
        assertEquals("{pair:(1,6789ABCDEF), pair2:(04,012345)}", RStr.toStr(printer));

        PairBean result = new BeanFromBytes<PairBean>().fromBytes(actual, PairBean.class, 0).getBean();
        assertEquals(pairBean, result);
    }
}
