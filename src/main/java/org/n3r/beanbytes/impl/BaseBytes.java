package org.n3r.beanbytes.impl;

import java.util.Map;

import org.n3r.beanbytes.BytesAware;
import org.n3r.beanbytes.BytesConverterAware;
import org.n3r.core.joor.Reflect;

import com.google.common.collect.Maps;

public abstract class BaseBytes<T> implements BytesAware<T> {
    protected Map<String, Object> options = Maps.newHashMap();
    protected BytesConverterAware<T> converter;

    public void addOptions(Map<String, Object> options) {
        this.options.putAll(options);
    }

    public void addOption(String name, Object option) {
        this.options.put(name, option);
    }

    public Object getOption(String optionName) {
        return options.get(optionName);
    }

    public Object getOption(String optionName, Object defaultValue) {
        Object optionValue = getOption(optionName);
        return optionValue != null ? optionValue : defaultValue;
    }

    public void setConverter(BytesConverterAware<T> converter) {
        this.converter = converter;
    }

    public BytesConverterAware<T> getConverter(Class<? extends BytesConverterAware> defConverterClass) {
        return converter != null ? converter : (BytesConverterAware<T>) Reflect.on(defConverterClass).create().get();
    }
}
