package com.example.entity;

/**
 * @author zhanghongjian
 * @Date 2019/4/16 15:47
 * @Description
 */
public class UrlEntity {

    private String url;
    private String fre;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFre() {
        return fre;
    }

    public void setFre(String fre) {
        this.fre = fre;
    }

    @Override
    public String toString() {
        return "UrlEntity{" +
                "url='" + url + '\'' +
                ", fre='" + fre + '\'' +
                '}';
    }
}
