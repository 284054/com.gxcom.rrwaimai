package com.gxcom.reggie.filter;

//检查用户是否完登录


import com.alibaba.fastjson.JSON;
import com.gxcom.reggie.common.BaseContext;
import com.gxcom.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFiler",urlPatterns = "/*")
@Slf4j
public class LoginChekFilter implements Filter {
    //路径通配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
       HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的UPI
        String requsertURI = request.getRequestURI();

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/employe/login",
                "/employe/logout",
                "/backend/**",
                "/business/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        //判断本次是否需要处理
        boolean check = check(urls, requsertURI);

        //如果不需要处理，直接放行
        if (check){
            log.info("本次请求{}不需要处理",requsertURI);
            filterChain.doFilter(request,response);
            return;
        }
        //判断登录状态，如果已经登录，直接放行
        if (request.getSession().getAttribute("employee") !=null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));


            Long empId = (Long) request.getSession().getAttribute("employee");

            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }
        //判断登录状态，如果已经登录，直接放行
        if (request.getSession().getAttribute("user") !=null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));


            Long userId = (Long) request.getSession().getAttribute("user");

            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);//放行
            return;
        }

        log.info("用户未登录");
        //如果没有登录
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }
    public boolean check(String[] urls, String requsertURI){
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url, requsertURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
