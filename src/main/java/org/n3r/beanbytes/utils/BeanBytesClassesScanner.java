package org.n3r.beanbytes.utils;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ClassUtils;
import org.joor.Reflect;
import org.n3r.beanbytes.FromBytesAware;
import org.n3r.beanbytes.ToBytesAware;
import org.n3r.beanbytes.annotations.JCApplyTo;
import org.n3r.beanbytes.annotations.JCBindType;
import org.reflections.Reflections;

import com.google.common.collect.Maps;

public class BeanBytesClassesScanner {
    private static Map<Class<?>, Class<?>> toBytesMapping = Maps.newHashMap();
    private static Map<Class<?>, Class<?>> fromBytesMapping = Maps.newHashMap();
    private static Set<Class<?>> jcApplyToClasses;
    static {
        Reflections reflections = new Reflections("org.n3r");
        jcApplyToClasses = reflections.getTypesAnnotatedWith(JCApplyTo.class);

        Set<Class<?>> registeredClasses = reflections.getTypesAnnotatedWith(JCBindType.class);
        for (Class<?> class1 : registeredClasses) {
            JCBindType toBytesRegister = class1.getAnnotation(JCBindType.class);

            if (ClassUtils.isAssignable(class1, ToBytesAware.class)) {
                toBytesMapping.put(toBytesRegister.value(), class1);
            }
            else if (ClassUtils.isAssignable(class1, FromBytesAware.class)) {
                fromBytesMapping.put(toBytesRegister.value(), class1);
            }
        }
    }

    public static Set<Class<?>> getJCApplyToClasses() {
        return jcApplyToClasses;
    }

    public static <T> ToBytesAware<T> getRegisteredToBytes(Class<?> clazz) {
        return getRegisteredClass(toBytesMapping, clazz);
    }

    public static <T> FromBytesAware<T> getRegisteredFromBytes(Class<?> clazz) {
        return getRegisteredClass(fromBytesMapping, clazz);
    }

    public static <T> T getRegisteredClass(Map<Class<?>, Class<?>> mapping, Class<?> clazz) {
        Class<?> class1 = mapping.get(clazz);
        if (class1 == null && clazz.isPrimitive()) class1 = mapping.get(ClassUtils.primitiveToWrapper(clazz));

        if (class1 == null) return null;

        return Reflect.on(class1).create().get();
    }

}
