package org.n3r.beanbytes.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.n3r.beanbytes.BytesAware;
import org.n3r.beanbytes.BytesConverterAware;
import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCOption;
import org.n3r.beanbytes.annotations.JCOptions;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.impl.BaseBytes;
import org.n3r.core.joor.Reflect;
import org.n3r.core.lang.RClass;
import org.n3r.core.lang.RStr;

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

        Class<?> converterClass = null;
        for (Class<?> clazz : BeanBytesClassesScanner.getApplyToClasses()) {
            JCApplyTo applyTo = clazz.getAnnotation(JCApplyTo.class);
            if (!field.isAnnotationPresent(applyTo.linked())) continue;
            if (!RClass.isAssignable(field.getType(), applyTo.value())) continue;

            converterClass = clazz;
            break;
        }

        if (converterClass != null) {
            BytesConverterAware<Object> converter = Reflect.on(converterClass).create().get();
            converter.setField(field);
            byteAware.setConverter(converter);
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

    public static void parseItemConverter(Class<?> clazz, BytesAware<Object> itemAware,
            BaseBytes<List<Object>> listAware) {
        Class<?> converterClass = null;
        Object itemLen = listAware.getOption("ItemLen", JCVarLen.class.getName());
        for (Class<?> apply : BeanBytesClassesScanner.getApplyToClasses()) {
            JCApplyTo applyTo = apply.getAnnotation(JCApplyTo.class);
            if (!Reflect.on(RStr.toStr(itemLen)).type().equals(applyTo.linked())) continue;
            if (!RClass.isAssignable(clazz, applyTo.value())) continue;

            converterClass = apply;
            break;
        }
        if (converterClass != null) {
            Reflect converter = Reflect.on(converterClass).create();
            Object[] params = new Object[0];
            if (itemLen.equals(JCFixLen.class.getName())) {
                params = new Object[] {
                        JCDataType.valueOf(RStr.toStr(listAware.getOption("ItemDataType", "HEX"))),
                        Integer.valueOf(RStr.toStr(listAware.getOption("ItemLength", "0"))),
                        RStr.toStr(listAware.getOption("ItemPad", "")),
                        RStr.toStr(listAware.getOption("ItemCharset", ""))
                };
            }
            else {
                params = new Object[] {
                        JCDataType.valueOf(RStr.toStr(listAware.getOption("ItemDataType", "HEX"))),
                        Integer.valueOf(RStr.toStr(listAware.getOption("ItemLenBytes", "1"))),
                        RStr.toStr(listAware.getOption("ItemCharset", ""))
                };
            }
            converter.call("setConvertParam", params);
            itemAware.setConverter((BytesConverterAware<Object>) converter.get());
        }
    }

}
