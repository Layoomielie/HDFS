package com.example;

/**
 * @author zhanghongjian
 * @Date 2019/3/28 15:07
 * @Description
 */
public class test {
    public static void main(String[] args) {
        String s="10.100.103.31 - - [28/Mar/2019:08:59:56 +0800] \"GET /static/js/common/common.js HTTP/1.1\" 304 0 \"http://10.100.23.84/ucenter/jxdInfo\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36\" \"-\" \"0.005\" \"0.005\" \"lano.connect.sid=s%3AVbqKISG8uZYubM_EiAZB_t5neHT03yrv.c8llrCoYmYBTYKRaGtTYFr2y8uERxf%2BgsMNDTFy63sY; centro.connect.sid=s%3AUOrd4LaQbunYrZi9Wn9qc9kfoG2xTD6I.a7Q3YlFrzneZ8G6GPwpvY9QpTXx7Ga79B8%2FRmsJ4YH4; atoken=7a5f3ed02c24cee4\"";
         String[] strs = s.split("\"");
        System.out.println(strs.length);
        for (String str:strs
             ) {
            System.out.println(str);
        }
    }
}
