package com.gracefullyugly.common.security.oauth2.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {

        if (attribute != null) {
            this.attribute = attribute;
        } else {
            System.out.println("attribute에 null 값들어옴");
            this.attribute = null; // 또는 적절한 처리를 해줍니다.
        }
    }

    @Override
    public String getProvider() {

        return "kakao";
    }

    @Override
    public String getProviderId() {

        if (attribute != null) {
            Object id = attribute.get("id");
            if (id != null) {
                return id.toString();
            }
            System.out.println("attribute 의 id가 null이야");
        }
        System.out.println("attribute 가 null 이야");
        return "attribute is null"; // 또는 적절한 처리를 해줍니다.
    }
}