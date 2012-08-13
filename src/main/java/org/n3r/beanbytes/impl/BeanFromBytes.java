package org.n3r.beanbytes.impl;

import java.lang.reflect.Field;

import org.joor.Reflect;
import org.n3r.beanbytes.FromByteBean;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.annotations.JCTransient;
import org.n3r.beanbytes.utils.BeanBytesClassesScanner;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RField;

public class BeanFromBytes<T> extends BaseBytes<T> implements FromBytesAware<T> {

    private int bytesSize;
    private int offset;

    @Override
    public FromByteBean<T> fromBytes(byte[] bytes, Class<?> clazz, int offset) {
        this.offset = offset;
        // 检查是否针对该类型已有转换器
        FromBytesAware<T> registeredFromBytes = BeanBytesClassesScanner.getBindFromBytes(clazz);
        if (registeredFromBytes != null) {
            registeredFromBytes.addOptions(options);
            FromByteBean<T> result = registeredFromBytes.fromBytes(bytes, clazz, offset);
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

        return new FromByteBean<T>(obj, bytesSize);
    }

    private Object parseFieldValue(byte[] bytes, Field field) {
        if (offset >= bytes.length) return null;

        FromBytesAware<Object> registeredFromBytes = BeanBytesClassesScanner.getBindFromBytes(field.getType());
        if (registeredFromBytes == null) {
            registeredFromBytes = new BeanFromBytes<Object>();
        }
        else {
            BeanBytesUtils.parseBeanBytes(field, registeredFromBytes);
            registeredFromBytes.addOption("FieldGenericType", field.getGenericType());
            registeredFromBytes.addOption("FieldName", field.getName());
        }

        FromByteBean<Object> result = registeredFromBytes.fromBytes(bytes, field.getType(), offset);
        offset += result.getBytesSize();
        bytesSize += result.getBytesSize();
        return result.getBean();
    }

}
