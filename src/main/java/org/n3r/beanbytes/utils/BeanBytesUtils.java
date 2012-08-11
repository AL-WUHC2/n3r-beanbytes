package org.n3r.beanbytes.utils;

import java.lang.reflect.Field;
import java.util.Map;

import org.joor.Reflect;
import org.n3r.beanbytes.BytesAware;
import org.n3r.beanbytes.BytesConverterAware;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCOption;
import org.n3r.beanbytes.annotations.JCOptions;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang.RClass;

import com.google.common.collect.Maps;

public class BeanBytesUtils {
    public static byte[] prependLen(byte[] src, int lenBytes) {
        return prependLen(src, lenBytes, src.length);
    }

    public static byte[] prependLen(byte[] src, int lenBytes, int lenValue) {
        byte[] lengthBytes = RByte.toBytes(lenValue);
        byte[] bytes = RByte.subBytes(lengthBytes, lengthBytes.length - lenBytes);

        return RByte.add(bytes, src);
    }

    public static void parseBeanBytes(Field field, BytesAware<Object> registeredToBytes) {
        registeredToBytes.addOptions(parseOptions(field));

        Class<?> converterClass = null;
        for (Class<?> class1 : BeanBytesClassesScanner.getJCApplyToClasses()) {
            JCApplyTo applyTo = class1.getAnnotation(JCApplyTo.class);
            if (!field.isAnnotationPresent(applyTo.linked())) continue;
            if (!RClass.isAssignable(field.getType(), applyTo.value())) continue;

            converterClass = class1;
            break;
        }

        if (converterClass != null) {
            BytesConverterAware converter = Reflect.on(converterClass).create().get();
            converter.setField(field);
            registeredToBytes.setConverter(converter);
        }
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
