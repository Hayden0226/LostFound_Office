package com.lostfound.config;

import com.lostfound.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行预检请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        boolean hasToken = authHeader != null && authHeader.startsWith("Bearer ");

        // 如果没有token且有token时才能提取用户信息
        if (hasToken) {
            String token = authHeader.substring(7);
            if (jwtUtils.validateToken(token)) {
                Claims claims = jwtUtils.parseToken(token);
                request.setAttribute("userId", Long.parseLong(claims.getSubject()));
                request.setAttribute("username", claims.get("username"));
                request.setAttribute("role", claims.get("role"));
            }
        }

        // GET请求允许公开访问，其他请求必须登录
        if (!"GET".equals(request.getMethod())) {
            if (!hasToken) {
                response.setStatus(401);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":401,\"msg\":\"未登录或token已过期\"}");
                return false;
            }
        }

        return true;
    }
}