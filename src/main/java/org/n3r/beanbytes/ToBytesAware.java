package org.n3r.beanbytes;


public interface ToBytesAware<T> extends BytesAware<T> {
    /**
     * 生成字节数组。
     * @param bean 对象
     * @param printer 打印字符串内容
     * @return
     */
    byte[] toBytes(T bean, StringBuilder printer);
}
