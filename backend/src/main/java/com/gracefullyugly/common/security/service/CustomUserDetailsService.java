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

        //DB에서 조회
        User userData = userRepository.findByLoginId(loginId);

        if (userData!= null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }

        return null;
    }
}