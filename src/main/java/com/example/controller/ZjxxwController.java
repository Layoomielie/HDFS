package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhanghongjian
 * @Date 2019/4/15 10:34
 * @Description
 */
@Controller
@RequestMapping
public class ZjxxwController {

    @RequestMapping("/index")
    public String index() {

        return "zjxxw";
    }

}
