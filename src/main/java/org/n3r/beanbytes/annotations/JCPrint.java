package org.n3r.beanbytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义字段的表示类型。
 * 当字段为定长字段@JCFixedLen并且需要字节补齐时，由此标注提供最终默认值(@JCFixedLen中的pad未指定).
 * @author Bingoo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JCPrint {
    /**
     * 表示类型。
     */
    JCPrintType value();
}
