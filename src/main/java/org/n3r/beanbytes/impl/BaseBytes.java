package org.n3r.beanbytes.impl;

import java.util.Map;

import org.n3r.beanbytes.BytesAware;
import org.n3r.beanbytes.BytesConverterAware;

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

    protected Object getOption(String optionName) {
        return options.get(optionName);
    }

    protected Object getOption(String optionName, Object defaultValue) {
        Object optionValue = getOption(optionName);
        return optionValue != null ? optionValue : defaultValue;
    }

    public void setConverter(BytesConverterAware<T> converter) {
        this.converter = converter;
    }

    public BytesConverterAware<T> getConverter(BytesConverterAware<T> defaultConverter) {
        return converter != null ? converter : defaultConverter;
    }
}
