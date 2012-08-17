package org.n3r.beanbytes.converter;

import java.util.List;

import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCListFixLen;

@JCApplyTo(value = List.class, linked = JCListFixLen.class)
public class JCListFixLenConverter extends JCListConverter<String> {

    @Override
    public void setListParam() {
        JCListFixLen jcListFixLen = (JCListFixLen) getAnnotation(JCListFixLen.class);
        listSizeBytes = jcListFixLen.listSizeBytes();
    }

    @Override
    public void setItemParam(Class<?> itemClass) {
        JCListFixLen jcListFixLen = (JCListFixLen) getAnnotation(JCListFixLen.class);
        JCFixLen itemLen = jcListFixLen.itemLen();
        JCStringFixLenConverter converter = new JCStringFixLenConverter();
        converter.setConvertParam(itemLen.dataType(), itemLen.length(), itemLen.pad(), itemLen.charset());
        itemConverter = converter;
    }

}
