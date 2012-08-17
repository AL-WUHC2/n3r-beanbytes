package org.n3r.beanbytes.converter;

import java.util.List;

import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCListVarLen;
import org.n3r.beanbytes.annotations.JCVarLen;

@JCApplyTo(value = List.class, linked = JCListVarLen.class)
public class JCListVarLenConverter extends JCListConverter<String> {

    @Override
    public void setListParam() {
        JCListVarLen jcListVarLen = (JCListVarLen) getAnnotation(JCListVarLen.class);
        listSizeBytes = jcListVarLen.listSizeBytes();
    }

    @Override
    public void setItemParam(Class<?> itemClass) {
        JCListVarLen jcListVarLen = (JCListVarLen) getAnnotation(JCListVarLen.class);
        JCVarLen itemLen = jcListVarLen.itemLen();
        JCStringVarLenConverter converter = new JCStringVarLenConverter();
        converter.setConvertParam(itemLen.dataType(), itemLen.lenBytes(), itemLen.charset());
        itemConverter = converter;
    }

}
