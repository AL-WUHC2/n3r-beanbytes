package org.n3r.beanbytes.converter;

public interface PairProcessAware<K, V> {

    void process(K key, JCPairConverter<K, V> jcPairConverter);

}
