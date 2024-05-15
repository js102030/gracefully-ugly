package com.gracefullyugly.domain.user.controller.api;

import static org.springframework.http.HttpStatus.CREATED;

import com.gracefullyugly.domain.user.dto.AdditionalRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegResponse;
import com.gracefullyugly.domain.user.dto.FinalRegResponse;
import com.gracefullyugly.domain.user.dto.ProfileResponse;
import com.gracefullyugly.domain.user.dto.UpdateAddressDto;
import com.gracefullyugly.domain.user.dto.UpdateNicknameDto;
import com.gracefullyugly.domain.user.dto.UpdatePasswordRequest;
import com.gracefullyugly.domain.user.dto.UserResponse;
import com.gracefullyugly.domain.user.dto.ValidEmail;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.service.UserSearchService;
import com.gracefullyugly.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="유저 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserSearchService userSearchService;

    @Operation(summary = "유저 회원가입 1단계", description = "사용할 아이디와 비밀번호 입력하기")
    @PostMapping(value = "/all/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BasicRegResponse> createBasicAccount(@RequestBody @Valid BasicRegRequest request) {
        BasicRegResponse basicRegResponse = userService.createBasicAccount(request);

        return ResponseEntity
                .status(CREATED)
                .body(basicRegResponse);
    }
    @Operation(summary = "유저 회원가입 2단계", description = "판매자 구매자 구분 & 이메일 입력하기")
    @PatchMapping(value = "/all/users/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FinalRegResponse> completeRegistration(@RequestBody @Valid AdditionalRegRequest request) {
        User user = userSearchService.findByloginId(request.getLoginId());
        FinalRegResponse finalRegResponse = userService.completeRegistration(user.getId(), request);

        return ResponseEntity
                .ok(finalRegResponse);
    }

    @Operation(summary = "유저 조회", description = "유저를 조회함")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        UserResponse userResponse = userSearchService.getUser(userId);

        return ResponseEntity
                .ok(userResponse);
    }

    @Operation(summary = "유저 프로필 조회", description = "유저 프로필을 조회함")
    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId) {
        ProfileResponse profileResponse = userSearchService.getProfile(userId);

        return ResponseEntity
                .ok(profileResponse);
    }

    @Operation(summary = "유저 닉네임 변경", description = "유저 닉네임을 변경함")
    @PatchMapping("/users/nickname/{userId}")
    public ResponseEntity<UserResponse> updateNickname(@PathVariable("userId") Long userId,
                                                       @Valid @RequestBody UpdateNicknameDto request) {
        UserResponse userResponse = userService.updateNickname(userId, request.getNickname());

        return ResponseEntity
                .ok(userResponse);
    }

    @Operation(summary = "유저 비밀번호 변경", description = "유저 비밀번호를 변경함")
    @PatchMapping("/users/password/{userId}")
    public ResponseEntity<Void> updatePassword(@PathVariable("userId") Long userId,
                                               @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(userId, request.getPassword());

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(summary = "유저 주소 변경", description = "유저 주소를 변경함")
    @PatchMapping("/users/address/{userId}")
    public ResponseEntity<UserResponse> updateAddress(@PathVariable("userId") Long userId,
                                                      @Valid @RequestBody UpdateAddressDto request) {
        UserResponse userResponse = userService.updateAddress(userId, request.getAddress());

        return ResponseEntity
                .ok(userResponse);
    }
    @Operation(summary = "유저 탈퇴", description = "유저가 서비스를 탈퇴함")
    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal(expression = "userId") Long userId) {
        userService.delete(userId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "유저 아이디 중복 검사", description = "유저가 변경하려는 아이디의 중복 검사 진행")
    @GetMapping(value = "/all/loginId-availability", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> checkLoginIdAvailability(@RequestParam String loginId) {
        return ResponseEntity
                .ok(userSearchService.existsByLoginId(loginId));
    }

    @Operation(summary = "유저 닉네임 중복 검사", description = "유저가 변경하려는 닉네임의 중복 검사 진행")
    @GetMapping(value = "/all/nickname-availability", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> checkNicknameAvailability(@RequestParam String nickname) {
        return ResponseEntity
                .ok(userSearchService.existsByNickName(nickname));
    }

    @Operation(summary = "유저 이메일 중복 검사", description = "유저가 변경하려는 이메일의 중복 검사 진행")
    @GetMapping("/email-availability")
    public ResponseEntity<Boolean> checkEmailAvailability(@Valid @RequestBody ValidEmail validEmail) {
        return ResponseEntity
                .ok(userSearchService.existsByEmail(validEmail.getEmail()));
    }

}
