package com.gracefullyugly.domain.user.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import com.gracefullyugly.domain.user.dto.AdditionalRegRequest;
import com.gracefullyugly.domain.user.dto.UpdateAddressDto;
import com.gracefullyugly.domain.user.dto.UpdateNicknameDto;
import com.gracefullyugly.domain.user.enumtype.Role;
import com.gracefullyugly.domain.user.enumtype.SignUpType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DynamicUpdate
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SignUpType signUpType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 30, unique = true)
    private String loginId;

    private String password;

    @Column(length = 30, unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String address;

    private boolean isBanned;

    private boolean isDeleted;

    private boolean isVerified;

    public User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
        this.signUpType = SignUpType.GENERAL;
    }

    public void completeRegistration(AdditionalRegRequest request) {
        this.role = request.getRole();
        this.nickname = request.getNickname();
        this.email = request.getEmail();
        this.address = request.getAddress();
    }

    public UpdateNicknameDto updateNickname(String newNickname) {
        this.nickname = newNickname;
        return new UpdateNicknameDto(newNickname);
    }

    public UpdateAddressDto updateAddress(String address) {
        this.address = address;
        return new UpdateAddressDto(address);
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void updateVerify(String email) {
        this.email = email;
        this.isVerified = true;
    }
}
