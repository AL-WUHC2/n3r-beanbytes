package org.n3r.beanbytes.utils;

import java.lang.reflect.Field;
import java.util.Map;

import org.n3r.beanbytes.BytesAware;
import org.n3r.beanbytes.BytesConverterAware;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCOption;
import org.n3r.beanbytes.annotations.JCOptions;
import org.n3r.beanbytes.converter.JCNullConverter;
import org.n3r.core.joor.Reflect;
import org.n3r.core.lang.RClass;

import com.google.common.collect.Maps;

import static org.n3r.core.lang.RByte.*;

public class BeanBytesUtils {
    public static byte[] prependLen(byte[] src, int lenBytes) {
        return prependLen(src, lenBytes, src.length);
    }

    public static byte[] prependLen(byte[] src, int lenBytes, int lenValue) {
        byte[] lengthBytes = toBytes(lenValue);
        byte[] bytes = subBytes(lengthBytes, lengthBytes.length - lenBytes);

        return add(bytes, src);
    }

    public static void parseBeanBytes(Field field, BytesAware<Object> byteAware) {
        byteAware.addOptions(parseOptions(field));

        byteAware.setConverter(parseConverter(field, field.getType()));
    }

    public static <T> BytesConverterAware<T> parseConverter(Field field, Class<?> clazz) {
        Class<?> converterClass = null;
        for (Class<?> apply : BeanBytesClassesScanner.getApplyToClasses()) {
            JCApplyTo applyTo = apply.getAnnotation(JCApplyTo.class);
            if (!field.isAnnotationPresent(applyTo.linked())) continue;
            if (!RClass.isAssignable(clazz, applyTo.value())) continue;

            converterClass = apply;
            break;
        }

        // 将Field信息存入ByteAware
        if (converterClass == null) return new JCNullConverter<T>().setField(field);

        return Reflect.on(converterClass).create().<BytesConverterAware<T>> get().setField(field);
    }

    private static Map<String, Object> parseOptions(Field field) {
        Map<String, Object> options = Maps.newHashMap();
        JCOption jcOption = field.getAnnotation(JCOption.class);
        if (jcOption != null) {
            options.put(jcOption.name(), jcOption.value());
        }

        JCOptions jcOptions = field.getAnnotation(JCOptions.class);
        if (jcOptions != null) {
            for (JCOption opt : jcOptions.value()) {
                options.put(opt.name(), opt.value());
            }
        }

        return options;
    }
}
