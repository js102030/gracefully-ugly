package com.gracefullyugly.domain.verification.controller;

import com.gracefullyugly.domain.verification.dto.VerifyRequest;
import com.gracefullyugly.domain.verification.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="이메일 인증")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class VerificationController {

    private final VerificationService verificationService;

    @Operation(summary = "인증 생성", description = "이메일 인증 코드 발송")
    @PostMapping("/verification")
    public ResponseEntity<String> sendEmailWithCode(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                    @RequestParam("email") String email) throws Exception {
        final String code = verificationService.sendMessageAndSaveCode(userId, email);

        log.info("인증코드 : " + code);

        return ResponseEntity
                .ok(code);
    }

    @Operation(summary = "인증 코드 확인", description = "인증된 회원으로 이메일 인증 상태 변경")
    @PatchMapping("/verification/check")
    public ResponseEntity<Void> validateCodeAndActivateEmail(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                             @RequestBody VerifyRequest verifyRequest) {
        verificationService.validateAndActivateEmailVerification(userId, verifyRequest);

        return ResponseEntity
                .ok()
                .build();
    }

}
