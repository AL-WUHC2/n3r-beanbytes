package com.ailk.phw.utils;

import org.n3r.beanbytes.converter.JCFixKeyPairProcess;
import org.n3r.beanbytes.converter.JCPairConverter;

public class IF5PairProcess extends JCFixKeyPairProcess {

    @Override
    public void process(String value, JCPairConverter<String, String> jcPairConverter) {
        if ("1".equals(value)) jcPairConverter.setVParams(6);

        else if ("2".equals(value)) jcPairConverter.setVParams(10);

        else jcPairConverter.setVParams(8);
    }

}
