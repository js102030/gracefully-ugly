package com.gracefullyugly.common.security.login.service;

import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String longId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(longId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디가 존재하지 않습니다."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLoginId())
                .password(user.getPassword())
                .roles("ROLE_" + user.getRole().name())
                .build();
    }
}