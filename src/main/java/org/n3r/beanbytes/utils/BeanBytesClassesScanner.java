package org.n3r.beanbytes.utils;

import java.util.Map;
import java.util.Set;

import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCBindType;
import org.n3r.core.joor.Reflect;
import org.reflections.Reflections;

import static com.google.common.collect.Maps.*;
import static org.n3r.core.lang3.ClassUtils.*;

public class BeanBytesClassesScanner {
    private static Map<Class<?>, Class<?>> toBytesMap = newHashMap();
    private static Map<Class<?>, Class<?>> toBeanMap = newHashMap();
    private static Set<Class<?>> applyToClasses;
    static {
        Reflections reflections = new Reflections("org.n3r");
        applyToClasses = reflections.getTypesAnnotatedWith(JCApplyTo.class);

        for (Class<?> clz : reflections.getTypesAnnotatedWith(JCBindType.class)) {
            JCBindType bindType = clz.getAnnotation(JCBindType.class);

            Class<?> byteClass = bindType.value();
            if (isAssignable(clz, ToBytesAware.class)) toBytesMap.put(byteClass, clz);
            if (isAssignable(clz, FromBytesAware.class)) toBeanMap.put(byteClass, clz);
        }
    }

    public static Set<Class<?>> getApplyToClasses() {
        return applyToClasses;
    }

    public static <T> ToBytesAware<T> getBindToBytes(Class<?> clazz) {
        return getBindClass(toBytesMap, clazz);
    }

    public static <T> FromBytesAware<T> getBindFromBytes(Class<?> clazz) {
        return getBindClass(toBeanMap, clazz);
    }

    public static <T> T getBindClass(Map<Class<?>, Class<?>> map, Class<?> target) {
        Class<?> clz = map.get(target);
        if (clz == null && target.isPrimitive()) clz = map.get(primitiveToWrapper(target));
        if (clz == null) return null;

        return Reflect.on(clz).create().get();
    }

}
