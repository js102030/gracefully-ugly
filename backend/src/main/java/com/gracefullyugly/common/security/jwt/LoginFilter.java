package com.gracefullyugly.common.security.jwt;

import com.gracefullyugly.common.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Iterator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String loginId = obtainUsername(request);
        String password = obtainPassword(request);

        logger.info("클라이언트 요청에서 loginId 추출 = " + loginId);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password,
                null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        logger.info("token에 담은 검증을 위한 AuthenticationManager로 전달");
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) {
        logger.info("로그인 성공");
        //UserDetailsS
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        //유저 정보
        String loginId = authentication.getName();
        Long userId = customUserDetails.getUserId();

        logger.info("userId 추출 =" + userId);
        logger.info("로그인 한 ID 추출" + loginId);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        logger.info("역할 가져옴 = " + role);

        String access = jwtUtil.createJwt("access", userId, loginId, role, 60 * 10 * 1000L); //10분
        String refresh = jwtUtil.createJwt("refresh", userId, loginId, role, 60 * 10 * 1000L); //24시간

        logger.info("token 로그인 성공하고 토큰 발급 완료 access토큰 = " + access);
        logger.info("token 로그인 성공하고 토큰 발급 완료 refresh토큰 = " + refresh);

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        logger.info("로그인 실패");
        response.setStatus(401);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);   //쿠키 생명주기
        //cookie.setSecure(true);    //https 통신하면 이 값 넣어줌
        //cookie.setPath("/");       //쿠키 적용될 범위
        cookie.setHttpOnly(true);    //js에서 해당 쿠키 접근 불가하게 막음

        return cookie;
    }
}