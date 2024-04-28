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
import com.gracefullyugly.domain.user.service.UserSearchService;
import com.gracefullyugly.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        final BasicRegResponse basicRegResponse = userService.createBasicAccount(request);

        return ResponseEntity
                .status(CREATED)
                .body(basicRegResponse);
    }

    @PatchMapping("/users/{userId}/registration")
    public ResponseEntity<FinalRegResponse> completeRegistration(@PathVariable Long userId,
                                                                 @RequestBody AdditionalRegRequest request) {
        final FinalRegResponse finalRegResponse = userService.completeRegistration(userId, request);

        return ResponseEntity
                .ok(finalRegResponse);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        final UserResponse userResponse = userSearchService.getUser(userId);

        return ResponseEntity
                .ok(userResponse);
    }

    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId) {
        final ProfileResponse profileResponse = userSearchService.getProfile(userId);

        return ResponseEntity
                .ok(profileResponse);
    }

    @PatchMapping("/users/{userId}/nickname")
    public ResponseEntity<UpdateNicknameDto> updateNickname(@PathVariable Long userId,
                                                            @Valid @RequestBody UpdateNicknameDto request) {
        final UpdateNicknameDto updatedNickname = userService.updateNickname(userId, request.getNickname());

        return ResponseEntity
                .ok(updatedNickname);
    }

    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long userId,
                                               @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(userId, request.getPassword());

        return ResponseEntity
                .ok()
                .build();
    }

    @PatchMapping("/users/{userId}/address")
    public ResponseEntity<UpdateAddressDto> updateAddress(@PathVariable Long userId,
                                                          @Valid @RequestBody UpdateAddressDto request) {
        final UpdateAddressDto updatedAddress = userService.updateAddress(userId, request.getAddress());

        return ResponseEntity
                .ok(updatedAddress);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);

        return ResponseEntity
                .noContent()
                .build();
    }

}
