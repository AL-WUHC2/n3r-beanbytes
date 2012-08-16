package org.n3r.beanbytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.n3r.beanbytes.JCDataType;

/**
 * 定义定长字段。
 * @author Bingoo
 *
 */
@JCExluding(JCVarLen.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JCFixLen {

    /**
     * 字段内容的数据类型。
     */
    JCDataType dataType() default JCDataType.HEX;

    /**
     * 定长字段的字节数目。
     * @return
     */
    int length();

    /**
     * 填充字节。
     * 例如：FF, 00等。
     * 此属性为可选值，如果不设置，刚依据@JCPrint定义决定其默认值。
     */
    String pad() default "";

    /**
     * 字段字符编码。
     */
    String charset() default "";
}
