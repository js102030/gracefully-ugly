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
import com.gracefullyugly.domain.user.dto.ValidLoginId;
import com.gracefullyugly.domain.user.dto.ValidNickname;
import com.gracefullyugly.domain.user.service.UserSearchService;
import com.gracefullyugly.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserSearchService userSearchService;

    @PostMapping("/users")
    public ResponseEntity<BasicRegResponse> createBasicAccount(@RequestBody BasicRegRequest request) {
        BasicRegResponse basicRegResponse = userService.createBasicAccount(request);

        return ResponseEntity
                .status(CREATED)
                .body(basicRegResponse);
    }

    @PatchMapping("/users/registration")
    public ResponseEntity<FinalRegResponse> completeRegistration(@RequestBody @Valid AdditionalRegRequest request,
                                                                 @AuthenticationPrincipal(expression = "userId") Long userId) {
        FinalRegResponse finalRegResponse = userService.completeRegistration(userId, request);

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

    @GetMapping("/loginId-availability")
    public ResponseEntity<Boolean> checkLoginIdAvailability(@Valid @RequestBody ValidLoginId validLoginId) {
        return ResponseEntity
                .ok(userSearchService.existsByLoginId(validLoginId.getLoginId()));
    }

    @GetMapping("/nickname-availability")
    public ResponseEntity<Boolean> checkNicknameAvailability(@Valid @RequestBody ValidNickname validNickname) {
        return ResponseEntity
                .ok(userSearchService.existsByNickName(validNickname.getNickname()));
    }

    @GetMapping("/email-availability")
    public ResponseEntity<Boolean> checkEmailAvailability(@Valid @RequestBody ValidEmail validEmail) {
        return ResponseEntity
                .ok(userSearchService.existsByEmail(validEmail.getEmail()));
    }

}
