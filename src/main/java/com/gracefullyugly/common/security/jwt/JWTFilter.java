package com.gracefullyugly.common.security.jwt;

import com.gracefullyugly.common.security.CustomUserDetails;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/swagger-ui/index.html",
                "/swagger-ui/swagger-ui-standalone-preset.js",
                "/swagger-ui/swagger-initializer.js",
                "/swagger-ui/swagger-ui-bundle.js",
                "/swagger-ui/swagger-ui.css",
                "/swagger-ui/index.css",
                "/swagger-ui/favicon-32x32.png",
                "/swagger-ui/favicon-16x16.png",
                "/api-docs/json/swagger-config",
                "/api-docs/json"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 쿠키에서 access키에 담긴 토큰을 꺼냄
        String token = null;
        Cookie[] cookies = request.getCookies();
        //Authorization 헤더 검증
        if (cookies == null) {

            logger.info("쿠기값 null");
            filterChain.doFilter(request, response);
            return;
        }

        logger.info("cookie null값 넘김, cookie =" + cookies);

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("token")) {

                token = cookie.getValue();
            }
        }

        //oauth2 하면서 자동으로 쿠키 갖고잇어서 이 로직 추가함
        if (token == null) {
            logger.info("토큰 없으니 아직 로그인 x");
            filterChain.doFilter(request, response);
            return;
        }
        logger.info("token = " + token);

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            logger.info("만료된 토큰으로 삭제");

            //만료되면 그냥 지워버림
            Cookie cookie = new Cookie("token", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");

            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/log"); // 재로그인 페이지로 리다이렉트
            //response status code 만료가 되면 그다음 필터로 안넘김
            return;
        }

        logger.info("만료시간 남앗음, 토큰 유효하니 통과");
        //토큰에서 username과 role 획득
        String loginId = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        Long userId = jwtUtil.getUserId(token);

        Role roleName = Role.fromRoleName(role);

        logger.info(loginId + "의 역할 = " + role);

        if (roleName == Role.GUEST || roleName == null) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/join2?loginId=" + loginId);
            return;
        }

        // userEntity를 빌더를 사용하여 생성하여 값 설정
        User userEntity = User.builder()
                .userId(userId)
                .loginId(loginId)
                .role(roleName)
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());

        logger.info("시큐리티 인증 토큰 = " + authToken);

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        logger.info("jwt 필터 종료");

        filterChain.doFilter(request, response);
    }


}