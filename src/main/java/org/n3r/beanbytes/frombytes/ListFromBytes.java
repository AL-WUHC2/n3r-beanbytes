package org.n3r.beanbytes.frombytes;

import java.lang.reflect.Type;
import java.util.List;

import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.converter.JCListVarLenConverter;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RType;

@JCBindType(List.class)
public class ListFromBytes extends BaseBytes<List<Object>> implements FromBytesAware<List<Object>> {

    @Override
    public ParseBean<List<Object>> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        int offset2 = offset;
        ParseBean<List<Object>> parseBean = getConverter(JCListVarLenConverter.class).decode(bytes, offset);
        offset2 += parseBean.getBytesSize();

        List<Object> result = parseBean.getBean();

        Class<?> itemClass = getItemClass();
        for (int i = 0; i < result.size(); i++) {
            BeanFromBytes<Object> beanFromBytes = new BeanFromBytes<Object>();

            BeanBytesUtils.parseItemConverter(itemClass, beanFromBytes, this);

            ParseBean<?> item = beanFromBytes.fromBytes(bytes, itemClass, offset2);
            offset2 += item.getBytesSize();
            result.set(i, item.getBean());
        }

        return new ParseBean<List<Object>>(result, offset2 - offset);
    }

    private Class<?> getItemClass() {
        Type genericType = (Type) getOption("FieldGenericType");
        Class<?> itemClass = RType.getActualTypeArgument(genericType);
        if (itemClass != Void.class) return itemClass;

        throw new RuntimeException("Unkown List Item Class for field " + getOption("FieldName"));
    }

}
