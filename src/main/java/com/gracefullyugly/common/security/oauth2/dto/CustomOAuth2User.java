package com.gracefullyugly.common.security.oauth2.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;

    public CustomOAuth2User(UserDTO userDTO) {

        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDTO.getRole().getRoleName();
            }
        });

        return collection;
    }

    public String getName() {

        return userDTO.getUsername();
    }

    public Long getUserId() {

        return userDTO.getUserId();
    }

    public String getAccessToken() {

        return userDTO.getAccessToken();
    }
}