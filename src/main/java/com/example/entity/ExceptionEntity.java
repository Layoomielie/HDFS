package com.example.entity;

/**
 * @author zhanghongjian
 * @Date 2019/4/12 10:54
 * @Description
 */
public class ExceptionEntity {
    private String url;
    private String code;
    private String freq;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    @Override
    public String toString() {
        return "ExceptionEntity{" +
                "url='" + url + '\'' +
                ", code='" + code + '\'' +
                ", freq='" + freq + '\'' +
                '}';
    }
}
