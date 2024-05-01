package com.gracefullyugly.common.security.jwt;

import com.gracefullyugly.common.security.CustomUserDetails;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");
        logger.info("authorization = " + authorization);

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            logger.info("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        logger.info("authorization null값 넘김, authorization =" + authorization + "/ token =" + token);

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            logger.info("token 만료");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }
        logger.info("만료시간 남앗음 통과");
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
                .password("temppassword")
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