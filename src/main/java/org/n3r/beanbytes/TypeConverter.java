package org.n3r.beanbytes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public abstract class TypeConverter<T> implements BytesConverterAware<T> {

    protected Field field;

    @Override
    public BytesConverterAware<T> setField(Field field) {
        this.field = field;
        return this;
    }

    @Override
    public Field getField() {
        return field;
    }

    public Annotation getAnnotation(Class<? extends Annotation> annotation) {
        return field == null ? null : field.getAnnotation(annotation);
    }

}
