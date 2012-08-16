package org.n3r.beanbytes.impl;

import java.lang.reflect.Field;

import org.joor.Reflect;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.annotations.JCTransient;
import org.n3r.beanbytes.utils.BeanBytesClassesScanner;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RField;

public class BeanFromBytes<T> extends BaseBytes<T> implements FromBytesAware<T> {

    private int bytesSize;
    private int offset;

    @Override
    public ParseBean<T> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        this.offset = offset;
        // 检查是否针对该类型已有转换器
        FromBytesAware<T> bindFromBytes = BeanBytesClassesScanner.getBindFromBytes(clazz);
        if (bindFromBytes != null) {
            bindFromBytes.addOptions(options);
            bindFromBytes.setConverter(converter);
            ParseBean<T> result = bindFromBytes.fromBytes(bytes, clazz, offset);
            return result;
        }

        // 当做JAVA BEAN处理，遍历fields
        T obj = Reflect.on(clazz).create().get();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(JCTransient.class)) continue;
            if (RField.isNotNormal(field)) continue;

            Object fieldValue = parseFieldValue(bytes, field);
            Reflect.on(obj).set(field.getName(), fieldValue);
        }

        return new ParseBean<T>(obj, bytesSize);
    }

    private Object parseFieldValue(byte[] bytes, Field field) {
        if (offset + bytesSize >= bytes.length) return null;

        FromBytesAware<Object> bindFromBytes = BeanBytesClassesScanner.getBindFromBytes(field.getType());
        if (bindFromBytes == null) {
            bindFromBytes = new BeanFromBytes<Object>();
        }
        else {
            BeanBytesUtils.parseBeanBytes(field, bindFromBytes);
            bindFromBytes.addOption("FieldGenericType", field.getGenericType());
            bindFromBytes.addOption("FieldName", field.getName());
        }

        ParseBean<Object> result = bindFromBytes.fromBytes(bytes, field.getType(), offset + bytesSize);
        bytesSize += result.getBytesSize();

        return result.getBean();
    }

}
