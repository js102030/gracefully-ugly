package com.gracefullyugly.common.security.config;


import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import com.gracefullyugly.common.security.CustomLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring().requestMatchers(toH2Console())
                .requestMatchers("/static/**", "swagger-ui/**", "/v3/api-docs/**", "swagger-ui.html");
    }

//    @Bean
//    public RoleHierarchy roleHierarchy() {
//
//        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
//
//        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_L2\n" +
//                "ROLE_L2 > ROLE_L1");
//
//        return hierarchy;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.
                authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .requestMatchers("/login", "/join", "users", "/api/**", "/")
                                .permitAll()
//                        .requestMatchers("/board").hasRole(L1.name())
//                        .requestMatchers("/my/**").hasAnyRole(L1.name())
//                        .requestMatchers("/board2/**").hasAnyRole(L2.name())
//                        .requestMatchers("/admin/**").hasAnyRole(ADMIN.name())
                                .anyRequest()
                                .authenticated()
                );

        httpSecurity.exceptionHandling(auth -> auth
                .accessDeniedPage("/access-denied")
        );

        httpSecurity
                .formLogin(auth -> auth
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                );

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);

        httpSecurity
                .sessionManagement(auth -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );

        httpSecurity
                .sessionManagement(auth -> auth
                        .sessionFixation()
                        .changeSessionId()
                );

        httpSecurity
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                );

        return httpSecurity.build();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}