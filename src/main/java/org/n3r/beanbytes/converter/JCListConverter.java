package org.n3r.beanbytes.converter;

import java.util.ArrayList;
import java.util.List;

import org.n3r.beanbytes.BytesConverterAware;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.TypeConverter;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCList;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang.RStr;
import org.n3r.core.lang.RType;

import static org.n3r.core.lang.RByte.*;

@JCApplyTo(value = List.class, linked = JCList.class)
public class JCListConverter<T> extends TypeConverter<List<T>> {
    protected static final String SEP = ", ";

    protected int listSizeBytes;
    protected BytesConverterAware<T> itemConverter;

    @Override
    public byte[] encode(List<T> value, StringBuilder printer) {
        setListParam();
        byte[] result = new byte[0];

        RStr.append(printer, '[');
        for (T item : value) {
            setItemParam(item.getClass());

            BeanToBytes<T> beanToBytes = new BeanToBytes<T>();
            beanToBytes.setConverter(itemConverter);

            byte[] itemBytes = beanToBytes.toBytes(item, printer);
            result = RByte.add(result, itemBytes);

            RStr.append(printer, SEP);
        }

        RStr.removeTail(printer, SEP);
        RStr.append(printer, ']');

        byte[] lengthBytes = toBytes(value.size());
        return add(subBytes(lengthBytes, lengthBytes.length - listSizeBytes), result);
    }

    @Override
    public ParseBean<List<T>> decode(byte[] bytes, int offset) {
        setListParam();
        Class<?> itemClass = getItemClass(null);
        setItemParam(itemClass);

        byte[] lenByte = subBytes(bytes, offset, listSizeBytes);
        int size = toInt(padHead(lenByte, 4));
        int offset2 = offset + listSizeBytes;

        List<T> result = new ArrayList<T>();
        for (int i = 0; i < size; i++) {
            BeanFromBytes<T> beanFromBytes = new BeanFromBytes<T>();
            beanFromBytes.setConverter(itemConverter);

            ParseBean<T> item = beanFromBytes.fromBytes(bytes, itemClass, offset2);
            offset2 += item.getBytesSize();
            result.add(item.getBean());
        }

        return new ParseBean<List<T>>(result, offset2 - offset);
    }

    public void setListParam() {
        setListParam(1);
    }

    public void setListParam(int listSizeBytes) {
        JCList jcList = (JCList) getAnnotation(JCList.class);
        this.listSizeBytes = jcList == null ? listSizeBytes : jcList.value();
    }

    public void setItemParam(Class<?> itemClass) {
        if (itemConverter != null) return;
        itemConverter = BeanBytesUtils.parseConverter(field, getItemClass(itemClass));
    }

    private Class<?> getItemClass(Class<?> itemClass) {
        if (itemClass != null) return itemClass;
        Class<?> clazz = RType.getActualTypeArgument(field.getGenericType());
        if (clazz != Void.class) return clazz;

        throw new RuntimeException("Unkown List Item Class for field " + field.getName());
    }
}
