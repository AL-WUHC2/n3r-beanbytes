package org.n3r.beanbytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.converter.JCFixKeyPairProcess;

/**
 * 定义根据其他字段变长的字段。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JCPairFixLen {

    /**
     * K字段定长类型。
     */
    JCFixLen kfieldType() default @JCFixLen(length = 1);

    /**
     * V字段数据类型。
     */
    JCDataType vdataType() default JCDataType.HEX;

    /**
     * V字段长度.
     */
    int vlength() default 0;

    /**
     * V字段填充字节。
     * 例如：FF, 00等。
     * 此属性为可选值，如果不设置，刚依据@JCPrint定义决定其默认值。
     */
    String vpad() default "";

    /**
     * V字段字符编码。
     */
    String vcharset() default "";

    /**
     * 根据K的值对V的处理器。
     */
    Class<? extends JCFixKeyPairProcess> processer() default JCFixKeyPairProcess.class;
}
