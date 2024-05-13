package com.gracefullyugly.domain.user.controller;

import com.gracefullyugly.domain.user.dto.SellerDetailsResponse;
import com.gracefullyugly.domain.user.service.SellerDetailsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SellerDetailsController {

    private final SellerDetailsService sellerDetailsService;

    @GetMapping("/api/sellerDetails/{itemId}")
    public ResponseEntity<List<SellerDetailsResponse>> getSellerDetails(@PathVariable("itemId") Long itemId) {
        List<SellerDetailsResponse> sellerDetails = sellerDetailsService.getSellerDetails(itemId);

        return ResponseEntity
                .ok(sellerDetails);
    }
}
