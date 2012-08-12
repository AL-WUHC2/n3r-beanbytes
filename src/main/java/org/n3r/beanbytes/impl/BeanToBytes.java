package org.n3r.beanbytes.impl;

import java.lang.reflect.Field;

import static org.apache.commons.lang3.Validate.*;
import org.joor.Reflect;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCPrint;
import org.n3r.beanbytes.annotations.JCTransient;
import org.n3r.beanbytes.utils.BeanBytesClassesScanner;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang.RField;
import org.n3r.core.lang.RHex;

public class BeanToBytes<T> extends BaseBytes<T> implements ToBytesAware<T> {
    private byte[] bytes = null;

    @Override
    public byte[] toBytes(T bean, StringBuilder printer) {
        // 检查是否针对该类型已有转换器
        ToBytesAware<T> registeredToBytes = BeanBytesClassesScanner.getRegisteredToBytes(bean.getClass());
        if (registeredToBytes != null) {
            registeredToBytes.addOptions(options);
            return registeredToBytes.toBytes(bean, printer);
        }

        // 当做JAVA BEAN处理，遍历fields
        bytes = new byte[0];
        Field lastField = null;
        if (printer != null) printer.append('{');
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(JCTransient.class)) continue;
            if (RField.isNotNormal(field)) continue;

            if (lastField != null) {
                processField(lastField, bean, printer, false);
                if (printer != null) printer.append(", ");
            }

            lastField = field;
        }

        // 最后一个字段，允许值为空
        if (lastField != null) processField(lastField, bean, printer, true);
        if (printer != null) printer.append('}');

        return bytes;
    }

    private void processField(Field field, T bean, StringBuilder printer, boolean nullable) {
        if (printer != null) printer.append(field.getName()).append(':');

        Object fieldValue = Reflect.on(bean).get(field.getName());
        if (fieldValue == null && nullable) return;
        notNull(fieldValue, "Field %s is not allowed null.", field.getName());

        ToBytesAware<Object> registeredToBytes = BeanBytesClassesScanner.getRegisteredToBytes(field.getType());
        if (registeredToBytes == null) {
            byte[] result = new BeanToBytes<Object>().toBytes(fieldValue, printer);
            bytes = RByte.add(bytes, result);
            return;
        }

        BeanBytesUtils.parseBeanBytes(field, registeredToBytes);

        JCPrint jcPrint = field.getAnnotation(JCPrint.class);
        byte[] result = registeredToBytes.toBytes(fieldValue, jcPrint != null ? null : printer);
        print(printer, result, fieldValue, field);

        bytes = RByte.add(bytes, result);
    }

    private void print(StringBuilder printer, byte[] result, Object fieldValue, Field field) {
        JCPrint jcPrint = field.getAnnotation(JCPrint.class);
        if (jcPrint == null) return;

        switch (jcPrint.value()) {
        case HEX:
            printer.append(RHex.encode(result));
            break;
        case Octet:
        case ASCII:
            printer.append("" + fieldValue);
            break;
        }
    }

}
