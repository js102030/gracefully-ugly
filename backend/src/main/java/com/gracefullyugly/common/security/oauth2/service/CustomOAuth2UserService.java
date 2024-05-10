package com.gracefullyugly.common.security.oauth2.service;

import com.gracefullyugly.common.security.controller.ReissueController;
import com.gracefullyugly.common.security.oauth2.dto.CustomOAuth2User;
import com.gracefullyugly.common.security.oauth2.dto.KakaoResponse;
import com.gracefullyugly.common.security.oauth2.dto.OAuth2Response;
import com.gracefullyugly.common.security.oauth2.dto.UserDTO;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.Role;
import com.gracefullyugly.domain.user.repository.UserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(ReissueController.class); // Logger 선언 추가

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        logger.info(String.valueOf(oAuth2User));

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }

        //추후 작성
        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        Optional<User> existData = userRepository.findByLoginId(username);

        if (!existData.isPresent()) {

            User userEntity = User.builder()
                    .loginId(username)
                    .role(Role.GUEST)
                    .build();

            User saveUser = userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setUserId(saveUser.getId());
            userDTO.setRole(saveUser.getRole()); // 일단 예시로 GUEST 해둠

            return new CustomOAuth2User(userDTO);
        } else {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.get().getLoginId());
            userDTO.setRole(existData.get().getRole());

            return new CustomOAuth2User(userDTO);
        }
    }
}