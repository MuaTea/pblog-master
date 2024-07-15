package com.niit.blog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title: LoginInterceptor.java
 * @Description: 后台系统身份验证拦截器
 */
@Component // 注解为Spring bean
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestServletPath = request.getServletPath();
        /**
         * 判断访问地址是否以“/user”开头
         * 如果是则需要判断 session 中是否有登录用户信息
         * 如果有则代表已登录，可以继续访问。
         */
        if (requestServletPath.startsWith("/user") && 
        		null == request.getSession().getAttribute("userId")) {
            request.getSession().setAttribute("errorMsg", "请重新登陆");
            response.sendRedirect(request.getContextPath() + "/user/login");
            return false;
        } else {
            request.getSession().removeAttribute("errorMsg");
            return true;
        }
    }
}
