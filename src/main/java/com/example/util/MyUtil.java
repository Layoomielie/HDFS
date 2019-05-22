package com.example.util;

/**
 * @author zhanghongjian
 * @Date 2019/4/11 19:40
 * @Description
 */
public class MyUtil {

    public static String removeBracket(String str) {
        return str.replace("(", "").replace("[", "").replace("<", "").replace(")", "").replace("]", "").replace(">", "");
    }


}
