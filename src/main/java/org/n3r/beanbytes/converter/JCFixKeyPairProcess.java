package org.n3r.beanbytes.converter;

import org.n3r.beanbytes.frombytes.primitives.IntegerFromBytes;
import org.n3r.core.lang.RHex;
import org.n3r.core.lang.RStr;

public class JCFixKeyPairProcess implements PairProcessAware<String, String> {

    @Override
    public void process(String key, JCPairConverter<String, String> jcPairConverter) {
        key = RStr.cutRight(key, 8, '0');
        int len = new IntegerFromBytes().fromBytes(RHex.decode(key), Integer.class, 0).getBean();

        jcPairConverter.setVParams(len);
    }

}
