package com.example.hbase;

/**
 * @author zhanghongjian
 * @Date 2019/4/10 16:14
 * @Description
 */
public class HbaseEntiy {
    // 列簇
    private String Family;
    // 限定名
    private String Qualifier;
    private String value;

    public String getFamily() {
        return Family;
    }

    public void setFamily(String family) {
        Family = family;
    }

    public String getQualifier() {
        return Qualifier;
    }

    public void setQualifier(String qualifier) {
        Qualifier = qualifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
