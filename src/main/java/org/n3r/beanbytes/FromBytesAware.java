package org.n3r.beanbytes;

public interface FromBytesAware<T> extends BytesAware<T> {

    FromByteBean<T> fromBytes(byte[] bytes, Class<?> clazz, int offset);

}
