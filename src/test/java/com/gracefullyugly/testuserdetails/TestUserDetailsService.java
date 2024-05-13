package com.gracefullyugly.testuserdetails;

import com.gracefullyugly.common.security.CustomUserDetails;
import com.gracefullyugly.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Profile("test")
public class TestUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public TestUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return new CustomUserDetails(userRepository.findByLoginId(loginId).get());
    }
}
