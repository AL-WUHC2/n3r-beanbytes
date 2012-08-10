package org.n3r.beanbytes.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Converter注解，表明适用于哪些类型的转换器。
 * @author Bingoo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JCApplyTo {

    Class<?>[] value();
    
    Class<? extends Annotation> linked() default Annotation.class;
}
