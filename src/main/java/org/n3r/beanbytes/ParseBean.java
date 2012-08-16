package org.n3r.beanbytes;

public class ParseBean<T> {

    private T bean;

    private int bytesSize;

    public ParseBean(T bean, int bytesSize) {
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
