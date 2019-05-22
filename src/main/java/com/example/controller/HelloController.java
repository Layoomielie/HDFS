package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanghongjian
 * @Date 2019/3/26 11:39
 * @Description
 */
@ResponseBody
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String getHello(){
        return "hello";
    }

    @RequestMapping("/list")
    public List getList(){
        List<String> list = new ArrayList<>();
        list.add("大象");
        list.add("猴子");
        list.add("蚂蚁");
        return list;
    }
}
