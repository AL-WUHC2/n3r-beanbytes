package org.n3r.beanbytes.utils;

import java.util.Map;
import java.util.Set;

import org.joor.Reflect;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCBindType;
import org.reflections.Reflections;

import static com.google.common.collect.Maps.*;
import static org.apache.commons.lang3.ClassUtils.*;

public class BeanBytesClassesScanner {
    private static Map<Class<?>, Class<?>> toBytesMaps = newHashMap();
    private static Map<Class<?>, Class<?>> toBeanMaps = newHashMap();
    private static Set<Class<?>> jcApplyToClasses;
    static {
        Reflections reflections = new Reflections("org.n3r");
        jcApplyToClasses = reflections.getTypesAnnotatedWith(JCApplyTo.class);

        Set<Class<?>> bindTypes = reflections.getTypesAnnotatedWith(JCBindType.class);
        for (Class<?> clz : bindTypes) {
            JCBindType bindType = clz.getAnnotation(JCBindType.class);

            if (isAssignable(clz, ToBytesAware.class)) toBytesMaps.put(bindType.value(), clz);
            if (isAssignable(clz, FromBytesAware.class)) toBeanMaps.put(bindType.value(), clz);
        }
    }

    public static Set<Class<?>> getJCApplyToClasses() {
        return jcApplyToClasses;
    }

    public static <T> ToBytesAware<T> getRegisteredToBytes(Class<?> clazz) {
        return getRegisteredClass(toBytesMaps, clazz);
    }

    public static <T> FromBytesAware<T> getRegisteredFromBytes(Class<?> clazz) {
        return getRegisteredClass(toBeanMaps, clazz);
    }

    public static <T> T getRegisteredClass(Map<Class<?>, Class<?>> mapping, Class<?> clazz) {
        Class<?> class1 = mapping.get(clazz);
        if (class1 == null && clazz.isPrimitive()) class1 = mapping.get(primitiveToWrapper(clazz));

        if (class1 == null) return null;

        return Reflect.on(class1).create().get();
    }

}
