package com.example.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author zhanghongjian
 * @Date 2019/4/15 10:06
 * @Description
 */

public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init  .....");

    }

    @Override
    public void destroy() {


    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


    }
}
