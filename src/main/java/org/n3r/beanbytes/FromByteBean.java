package org.n3r.beanbytes;

public class FromByteBean<T> {

    private T bean;

    private int bytesSize;

    public void setBean(T bean) {
        this.bean = bean;
    }

    public T getBean() {
        return bean;
    }

    public void setBytesSize(int bytesSize) {
        this.bytesSize = bytesSize;
    }

    public int getBytesSize() {
        return bytesSize;
    }

}
