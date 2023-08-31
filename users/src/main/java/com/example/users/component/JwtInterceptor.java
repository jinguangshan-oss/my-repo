package com.example.users.component;

import com.example.users.utils.JwtUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization"); // 获取请求头中的 JWT

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去除 "Bearer " 前缀

            // 验证 JWT 签名
            boolean flag = JwtUtils.validateToken(token);
            if(flag){
                return true;
            }else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 认证失败，返回 401 Unauthorized
                return false;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 没有提供有效的 JWT，返回 401 Unauthorized
            return false;
        }
    }
}

