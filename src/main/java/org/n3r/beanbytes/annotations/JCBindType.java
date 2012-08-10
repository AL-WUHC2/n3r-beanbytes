package org.n3r.beanbytes.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �󶨾�������tobytes, frombytesʵ�֡�
 * @author Bingoo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JCBindType {

    Class<?> value();
}
