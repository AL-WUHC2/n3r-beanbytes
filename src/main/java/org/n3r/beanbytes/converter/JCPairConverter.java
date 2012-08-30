package org.n3r.beanbytes.converter;

import org.apache.commons.lang3.tuple.Pair;
import org.n3r.beanbytes.TypeConverter;
import org.n3r.core.joor.Reflect;

public abstract class JCPairConverter<K, V> extends TypeConverter<Pair<K, V>> {

    private Class<? extends PairProcessAware<K, V>> processer;

    public abstract void setVParams(Object... params);

    protected void doProcess(K value) {
        Reflect.on(processer).create().<PairProcessAware<K, V>> get().process(value, this);
    }

    public void setProcesser(Class<? extends PairProcessAware<K, V>> processer) {
        this.processer = processer;
    }

}
