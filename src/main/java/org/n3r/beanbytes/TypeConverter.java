package org.n3r.beanbytes;

import java.lang.reflect.Field;

public abstract class TypeConverter<T> implements BytesConverterAware<T> {

    protected Field field;

    @Override
    public void setField(Field field) {
        this.field = field;
    }


}
