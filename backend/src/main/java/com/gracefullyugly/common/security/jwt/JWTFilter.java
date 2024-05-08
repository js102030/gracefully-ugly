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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 쿠키에서 access키에 담긴 토큰을 꺼냄
        String token = null;
        Cookie[] cookies = request.getCookies();
        //Authorization 헤더 검증
        if (cookies == null) {

            logger.info("accessToken null");
            filterChain.doFilter(request, response);

            //TODO 이 부분에서 /reissue api 요청해서 토큰 재발급 해줘야함
            return;
        }

        logger.info("cookie null값 넘김, cookie =" + cookies);

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("token")) {

                token = cookie.getValue();
            }
        }
        logger.info("token = " + token);

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            logger.info("토큰 만료됨");

            //TODO 이 부분에서 /reissue api 요청해서 토큰 재발급 해줘야함

            //response status code 만료가 되면 그다음 필터로 안넘김
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        logger.info("만료시간 남앗음, 토큰 유효하니 통과");
        //토큰에서 username과 role 획득
        String loginId = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        Long userId = jwtUtil.getUserId(token);

        Role roleName = Role.fromRoleName(role);

        logger.info(loginId + "의 역할 = " + role);

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