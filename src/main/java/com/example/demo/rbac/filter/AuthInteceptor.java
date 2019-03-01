package com.example.demo.rbac.filter;

import com.example.demo.rbac.anno.RequireAuth;
import com.example.demo.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class AuthInteceptor extends HandlerInterceptorAdapter {
    //https://blog.csdn.net/qq_25188255/article/details/79537849
    //https://www.cnblogs.com/yingsong/p/8508700.html
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getRequestURI());
        System.out.println(handler instanceof HandlerMethod);
        HandlerMethod methodHalder=(HandlerMethod)handler;
        RequireAuth auth = methodHalder.getMethodAnnotation(RequireAuth.class);
        if (auth != null && auth.check()){
            if ((User)request.getSession().getAttribute("loginUser") == null) {
                response.setStatus(401);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().print("{\"title\":\"用户权限异常\",\"exception\":null,\"message\":\"未登录\"}");
                return false;
            }
        }
        return true;
    }
}
