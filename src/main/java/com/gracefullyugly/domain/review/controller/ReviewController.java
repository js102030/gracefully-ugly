package com.gracefullyugly.domain.review.controller;

import com.gracefullyugly.domain.image.service.ImageUploadService;
import com.gracefullyugly.domain.review.dto.ReviewDto;
import com.gracefullyugly.domain.review.dto.ReviewResponse;
import com.gracefullyugly.domain.review.dto.ReviewWithImageResponse;
import com.gracefullyugly.domain.review.service.ReviewSearchService;
import com.gracefullyugly.domain.review.service.ReviewService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name="상품 후기 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewSearchService reviewSearchService;
    private final ImageUploadService imageUploadService;

    @Operation(summary = "후기 작성", description = "상품에 대한 후기를 작성함")
    @PostMapping("/reviews/{itemId}")
    public ResponseEntity<ReviewResponse> createReview(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                       @PathVariable Long itemId,
                                                       @ModelAttribute ReviewDto reviewDto,
                                                       @RequestPart(value = "productImage", required = false) MultipartFile file) {
        ReviewResponse response = reviewService.createReview(userId, reviewDto, itemId);

        if (file != null && !file.isEmpty()) {
            imageUploadService.saveFile(file, null, response.getReviewId());
        }

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "후기 단건 조회", description = "상품에 대한 후기를 단건 조회함")
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> findReview(@PathVariable Long reviewId) {
        ReviewResponse response = reviewSearchService.getReviewById(reviewId);

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "후기 목록 조회", description = "상품에 대한 후기를 목록 조회함")
    @GetMapping("/reviews/items/{itemId}")
    public ResponseEntity<List<ReviewWithImageResponse>> findReviewsByItemId(@PathVariable Long itemId) {
        List<ReviewWithImageResponse> response = reviewSearchService.getReviewsWithImagesByItemId(itemId);

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "후기 수정", description = "상품에 대한 후기를 수정함 - 추후 기능 추가")
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                       @PathVariable Long reviewId,
                                                       @RequestBody ReviewDto request) {
        ReviewResponse response = reviewService.updateReview(userId, reviewId, request);

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "후기 삭제", description = "상품에 대한 후기를 삭제함 - 추후 기능 추가")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal(expression = "userId") Long userId,
                                             @PathVariable Long reviewId) {
        reviewService.deleteReview(userId, reviewId);

        return ResponseEntity
                .noContent()
                .build();
    }

}
