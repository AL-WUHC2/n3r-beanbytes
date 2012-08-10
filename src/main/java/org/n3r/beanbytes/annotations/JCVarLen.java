package org.n3r.beanbytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义变长字符串长度所需要的字节数目。
 * @author Bingoo
 *
 */
@JCExluding(JCFixLen.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JCVarLen {
    /**
     * 表示长度需要占用的字节数。
     * 一般为1/2/4等。
     * @return
     */
    int value();

}
