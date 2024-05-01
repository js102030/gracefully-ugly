package com.gracefullyugly.common.security.config;

import com.gracefullyugly.common.security.jwt.JWTFilter;
import com.gracefullyugly.common.security.jwt.JWTUtil;
import com.gracefullyugly.common.security.jwt.LoginFilter;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리 JwtAuthenticationProcessingFilter는
 * AccessToken, RefreshToken 재발급
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer configure() {      // 스프링 시큐리티 기능 비활성화
        return web -> web.ignoring()  //.requestMatchers(toH2Console())
                .requestMatchers("/static/**", "/vendor/**", "/css/**", "/img/**", "/js/**", "/favicon.ico",
                        "/h2-console/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //front 단이랑 data 안보이는 오류 발생 막기위해서 CORS 설정
        http
                .cors(corsCustomizer -> corsCustomizer
                        .configurationSource(request -> {

                            CorsConfiguration configuration = new CorsConfiguration();

                            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                            configuration.setAllowedMethods(Collections.singletonList("*"));
                            configuration.setAllowCredentials(true);
                            configuration.setAllowedHeaders(Collections.singletonList("*"));
                            configuration.setMaxAge(3600L);

                            configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                            configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                            return configuration;
                        }));

        //csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth
                        .usernameParameter("loginId") // 변경된 부분
                        .disable());

        //http basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .httpBasic(AbstractHttpConfigurer::disable);

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/api/users", "/api/users/{userId}/registration", "/**")
                        .permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());

        //JWTFilter 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        //필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);
        //세션 설정 jwt 위해서는 STATELESS 로 설정 해줘야함
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}