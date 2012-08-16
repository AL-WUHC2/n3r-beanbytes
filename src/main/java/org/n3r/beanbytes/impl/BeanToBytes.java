package org.n3r.beanbytes.impl;

import java.lang.reflect.Field;

import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCTransient;
import org.n3r.beanbytes.utils.BeanBytesClassesScanner;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.joor.Reflect;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang.RField;
import org.n3r.core.lang.RStr;

import static org.n3r.core.lang3.Validate.*;

public class BeanToBytes<T> extends BaseBytes<T> implements ToBytesAware<T> {
    private byte[] bytes = null;

    @Override
    public byte[] toBytes(T bean, StringBuilder printer) {
        // 检查是否针对该类型已有转换器
        ToBytesAware<T> bindToBytes = BeanBytesClassesScanner.getBindToBytes(bean.getClass());
        if (bindToBytes != null) {
            bindToBytes.addOptions(options);
            bindToBytes.setConverter(converter);
            return bindToBytes.toBytes(bean, printer);
        }

        // 当做JAVA BEAN处理，遍历fields
        bytes = new byte[0];
        Field lastField = null;
        RStr.append(printer, '{');
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(JCTransient.class)) continue;
            if (RField.isNotNormal(field)) continue;

            if (lastField != null) {
                processField(lastField, bean, printer, false);
                RStr.append(printer, ", ");
            }

            lastField = field;
        }

        // 最后一个字段，允许值为空
        if (lastField != null) processField(lastField, bean, printer, true);
        RStr.append(printer, '}');

        return bytes;
    }

    private void processField(Field field, T bean, StringBuilder printer, boolean nullable) {
        RStr.append(printer, field.getName() + ':');

        Object fieldValue = Reflect.on(bean).get(field.getName());
        if (fieldValue == null && nullable) return;
        notNull(fieldValue, "Field %s is not allowed null.", field.getName());

        ToBytesAware<Object> bindToBytes = BeanBytesClassesScanner.getBindToBytes(field.getType());
        if (bindToBytes == null) {
            byte[] result = new BeanToBytes<Object>().toBytes(fieldValue, printer);
            bytes = RByte.add(bytes, result);
            return;
        }

        BeanBytesUtils.parseBeanBytes(field, bindToBytes);

        byte[] result = bindToBytes.toBytes(fieldValue, printer);

        bytes = RByte.add(bytes, result);
    }

}
