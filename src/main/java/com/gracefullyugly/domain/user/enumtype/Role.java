package com.gracefullyugly.domain.user.enumtype;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Role {
    //첫 로그인 구별하려고 GUEST 추가
    ADMIN("ROLE_ADMIN"),
    BUYER("ROLE_BUYER"),
    SELLER("ROLE_SELLER"),
    GUEST("ROLE_GUEST");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    // 이전에 사용한 valueOf 메서드 대신, roleName으로 역할을 가져오는 메서드를 추가할 수 있습니다.
    public static Role fromRoleName(String roleName) {
        for (Role role : Role.values()) {
            if (role.getRoleName().equals(roleName)) {
                log.info("rolename=" + roleName);
                log.info("/role=" + role);
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant com.gracefullyugly.domain.user.enumtype.Role with roleName " + roleName);
    }

}
