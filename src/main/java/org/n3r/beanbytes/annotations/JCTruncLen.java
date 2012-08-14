package org.n3r.beanbytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义从高位截断的字段。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JCTruncLen {

    /**
     * 定义截断的高位字节数.
     * @return
     */
    int value();

    /**
     * 定义还原时高位填充的字节.
     */
    String pad() default "00";

}
