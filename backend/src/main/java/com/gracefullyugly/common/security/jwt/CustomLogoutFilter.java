package com.gracefullyugly.common.security.jwt;

import com.gracefullyugly.domain.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.GenericFilterBean;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public CustomLogoutFilter(JWTUtil jwtUtil, UserRepository userRepository) {

        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            logger.info("로그아웃 요청은 POST로 하세여");
            filterChain.doFilter(request, response);
            return;
        }

        //get token
        String token = null;
        String kakao = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("token")) {

                token = cookie.getValue();
            } else if (cookie.getName().equals("kakao")) {
                kakao = cookie.getValue();
            }
        }

        //refresh null check
        if (token == null) {
            logger.info("이미 로그아웃 되셧어여");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //loginId 꺼내옴
        String loginId = jwtUtil.getUsername(token); // 예를 들어서 loginId를 추출하는 방법
        //accessToken  꺼내옴
        String accessToken = jwtUtil.getToken(token); // 예를 들어서 loginId를 추출하는 방법
        logger.info("js로 넘겨준 accessToken=" + accessToken);

        //로그아웃 진행

        //토큰 Cookie 값 0
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        if (kakao != null) {
            //토큰 Cookie 값 0
            Cookie kakao_cookie = new Cookie("kakao", null);
            kakao_cookie.setMaxAge(0);
            kakao_cookie.setPath("/");

            response.addCookie(kakao_cookie);
        }

        response.addCookie(cookie);

        // AccessToken을 응답으로 보내줌
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{ \"accessToken\": \"" + accessToken + "\" }");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}