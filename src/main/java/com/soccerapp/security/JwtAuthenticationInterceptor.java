package com.soccerapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    public JwtAuthenticationInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer")){
            try{
                String email = jwtUtil.getEmailFromToken(token.substring(7));
                request.setAttribute("email", email);
            } catch(Exception e) {
                System.out.println("Failed to extract email from token: " + e.getMessage());
            }
        }
        return true;
    }
}
