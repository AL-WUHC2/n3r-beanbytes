package org.n3r.beanbytes.frombytes;

import java.lang.reflect.Type;
import java.util.List;

import org.n3r.beanbytes.FromByteBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.ByteBean;
import org.n3r.core.lang.RField;

import com.google.common.collect.Lists;

@JCBindType(List.class)
public class ListFromBytes extends BaseBytes<List<Object>> implements FromBytesAware<List<Object>> {

    @Override
    public FromByteBean<List<Object>> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        FromByteBean<List<Object>> ret = new FromByteBean<List<Object>>();
        Class<?> itemClass = getItemClass();

        ByteBean byteBean = converter.decode(bytes, offset);
        int len = byteBean.getProcessBytesNum();
        offset += byteBean.getProcessBytesNum();

        List<Object> result = Lists.newArrayList();
        for (int i = 0; i < byteBean.getSize(); i++) {
            BeanFromBytes<?> beanFromBytes = new BeanFromBytes<Object>();
            FromByteBean<?> item = beanFromBytes.fromBytes(bytes, itemClass, offset);
            offset += item.getBytesSize();
            result.add(item.getBean());
            len += item.getBytesSize();
        }

        ret.setBean(result);
        ret.setBytesSize(len);
        return ret;
    }

    private Class<?> getItemClass() {
        Type genericType = (Type) getOption("FieldGenericType");
        Class<?> itemClass = RField.getActualTypeArgument(genericType);
        if (itemClass != Void.class) return itemClass;

        throw new RuntimeException("Unkown List Item Class for field " + getOption("FieldName"));
    }

}
