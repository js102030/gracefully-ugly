package com.gracefullyugly.common.security.controller;

import com.gracefullyugly.common.security.jwt.JWTUtil;
import com.gracefullyugly.domain.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Import 추가

@Controller
@ResponseBody
public class ReissueController {

    private final JWTUtil jwtUtil;

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReissueController.class); // Logger 선언 추가

    public ReissueController(JWTUtil jwtUtil, UserRepository userRepository) {

        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            logger.info("refresh가 null이야");
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            logger.info("refresh가 유효기간 만료됨");
            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            logger.info("보낸게 refresh토큰 아닌데?");
            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String loginId = jwtUtil.getUsername(refresh);
        Long userId = jwtUtil.getUserId(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access",userId, loginId, role, 60 * 10 * 1000L);
        String newRefresh = jwtUtil.createJwt("refresh", userId, loginId    , role, 86400000L);

        userRepository.saveRefreshToken(loginId, newRefresh);
        String saveRefresh = userRepository.findRefreshTokenByLoginId(loginId);
        logger.info("기존 refresh =" + refresh) ;
        logger.info("재발급한 refresh =" + newRefresh) ;
        logger.info("저장한 refresh =" + saveRefresh) ;

        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}