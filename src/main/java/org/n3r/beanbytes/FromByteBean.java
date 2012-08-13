package org.n3r.beanbytes;

public class FromByteBean<T> {

    private T bean;

    private int bytesSize;

    public FromByteBean(T bean, int bytesSize) {
        this.bean = bean;
        this.bytesSize = bytesSize;
    }

    public T getBean() {
        return bean;
    }

    public int getBytesSize() {
        return bytesSize;
    }

}
