package com.gracefullyugly.domain.user.service;

import com.gracefullyugly.domain.user.dto.SellerDetailsResponse;
import com.gracefullyugly.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerDetailsService {

    private final UserRepository userRepository;

    public List<SellerDetailsResponse> getSellerDetails(Long itemId) {
        return userRepository.findSellerDetails(itemId);
    }
}
