package org.n3r.beanbytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.n3r.beanbytes.JCDataType;

/**
 * 定义变长字段。
 * @author Bingoo
 *
 */
@JCExluding(JCFixLen.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JCVarLen {

    /**
     * 字段内容的数据类型。
     */
    JCDataType dataType() default JCDataType.HEX;

    /**
     * 表示长度需要占用的字节数。
     * 一般为1/2/4等。
     * @return
     */
    int lenBytes() default 1;

    /**
     * 字段字符编码。
     */
    String charset() default "";
}
