package org.n3r.beanbytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义定长字段的字节数目。
 * @author Bingoo
 *
 */
@JCExluding(JCVarLen.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JCFixLen {

    /**
     * 定长字段的字节数目。
     * @return
     */
    int value();


    /**
     * 填充字节。
     * 例如：FF, 00等。
     * 此属性为可选值，如果不设置，刚依据@JCPrint定义决定其默认值。
     */
    String pad() default "";
}
