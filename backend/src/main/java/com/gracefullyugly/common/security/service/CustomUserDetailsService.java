package com.gracefullyugly.common.security.service;

import com.gracefullyugly.common.security.CustomUserDetails;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        // DB에서 조회
        Optional<User> userDataOptional = userRepository.findByLoginId(loginId);

        // Optional에서 User 객체를 가져오거나, 값이 없으면 예외를 던집니다.
        User userData = userDataOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with loginId: " + loginId));

        // UserDetails에 담아서 return하면 AuthenticationManager가 검증 함
        return new CustomUserDetails(userData);
    }
}