package org.n3r.beanbytes;

import java.lang.reflect.Field;

public abstract class TypeConverter implements BytesConverterAware {

    protected Field field;

    @Override
    public void setField(Field field) {
        this.field = field;
    }


}
