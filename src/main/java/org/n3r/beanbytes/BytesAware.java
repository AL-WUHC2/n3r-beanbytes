package org.n3r.beanbytes;

import java.util.Map;

public interface BytesAware<T> {

    /**
     * 设置转换选项。
     * 例如设置charsetName=UTF-16LE。
     * @param options
     */
    void addOptions(Map<String, Object> options);


    void addOption(String name, Object option);
    
    /**
     * 设置转换器。
     * 例如填充指定长度的转换器等。
     * @param converter
     */
    void setConverter(BytesConverterAware converter);

}
