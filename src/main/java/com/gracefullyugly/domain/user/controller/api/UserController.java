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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserSearchService userSearchService;

    @PostMapping(value = "/all/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BasicRegResponse> createBasicAccount(@RequestBody @Valid BasicRegRequest request) {
        BasicRegResponse basicRegResponse = userService.createBasicAccount(request);

        return ResponseEntity
                .status(CREATED)
                .body(basicRegResponse);
    }

    @PatchMapping(value = "/all/users/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FinalRegResponse> completeRegistration(@RequestBody @Valid AdditionalRegRequest request) {
        User user = userSearchService.findByloginId(request.getLoginId());
        FinalRegResponse finalRegResponse = userService.completeRegistration(user.getId(), request);

        return ResponseEntity
                .ok(finalRegResponse);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        UserResponse userResponse = userSearchService.getUser(userId);

        return ResponseEntity
                .ok(userResponse);
    }

    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId) {
        ProfileResponse profileResponse = userSearchService.getProfile(userId);

        return ResponseEntity
                .ok(profileResponse);
    }

    @PatchMapping("/users/nickname")
    public ResponseEntity<UserResponse> updateNickname(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                       @Valid @RequestBody UpdateNicknameDto request) {
        UserResponse userResponse = userService.updateNickname(userId, request.getNickname());

        return ResponseEntity
                .ok(userResponse);
    }

    @PatchMapping("/users/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal(expression = "userId") Long userId,
                                               @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(userId, request.getPassword());

        return ResponseEntity
                .ok()
                .build();
    }

    @PatchMapping("/users/address")
    public ResponseEntity<UserResponse> updateAddress(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                      @Valid @RequestBody UpdateAddressDto request) {
        UserResponse userResponse = userService.updateAddress(userId, request.getAddress());

        return ResponseEntity
                .ok(userResponse);
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal(expression = "userId") Long userId) {
        userService.delete(userId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(value = "/all/loginId-availability", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> checkLoginIdAvailability(@RequestParam String loginId) {
        return ResponseEntity
                .ok(userSearchService.existsByLoginId(loginId));
    }

    @GetMapping(value = "/all/nickname-availability", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> checkNicknameAvailability(@RequestParam String nickname) {
        return ResponseEntity
                .ok(userSearchService.existsByNickName(nickname));
    }

    @GetMapping("/email-availability")
    public ResponseEntity<Boolean> checkEmailAvailability(@Valid @RequestBody ValidEmail validEmail) {
        return ResponseEntity
                .ok(userSearchService.existsByEmail(validEmail.getEmail()));
    }

}
