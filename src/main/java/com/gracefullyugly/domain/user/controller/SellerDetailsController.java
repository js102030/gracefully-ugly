package com.gracefullyugly.domain.user.controller;

import com.gracefullyugly.domain.user.dto.SellerDetailsResponse;
import com.gracefullyugly.domain.user.service.SellerDetailsService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
@Tag(name="판매자 상세페이지 주문 정보 관리")
@RestController
@RequiredArgsConstructor
@Slf4j
public class SellerDetailsController {

    private final SellerDetailsService sellerDetailsService;

    @Operation(summary = "판매자 상세페이지에 상품 정보 제공", description = "특정 상품에 대한 주문 정보를 제공함")
    @GetMapping("/api/sellerDetails/{itemId}")
    public ResponseEntity<List<SellerDetailsResponse>> getSellerDetails(@PathVariable("itemId") Long itemId) {
        List<SellerDetailsResponse> sellerDetails = sellerDetailsService.getSellerDetails(itemId);

        return ResponseEntity
                .ok(sellerDetails);
    }
}
