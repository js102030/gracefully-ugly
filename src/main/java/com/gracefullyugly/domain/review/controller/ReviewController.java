package com.gracefullyugly.domain.review.controller;

import com.gracefullyugly.domain.image.service.ImageUploadService;
import com.gracefullyugly.domain.review.dto.ReviewDto;
import com.gracefullyugly.domain.review.dto.ReviewResponse;
import com.gracefullyugly.domain.review.dto.ReviewWithImageResponse;
import com.gracefullyugly.domain.review.service.ReviewSearchService;
import com.gracefullyugly.domain.review.service.ReviewService;
import java.util.List;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewSearchService reviewSearchService;
    private final ImageUploadService imageUploadService;

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

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> findReview(@PathVariable Long reviewId) {
        ReviewResponse response = reviewSearchService.getReviewById(reviewId);

        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/reviews/items/{itemId}")
    public ResponseEntity<List<ReviewWithImageResponse>> findReviewsByItemId(@PathVariable Long itemId) {
        List<ReviewWithImageResponse> response = reviewSearchService.getReviewsWithImagesByItemId(itemId);

        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                       @PathVariable Long reviewId,
                                                       @RequestBody ReviewDto request) {
        ReviewResponse response = reviewService.updateReview(userId, reviewId, request);

        return ResponseEntity
                .ok(response);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal(expression = "userId") Long userId,
                                             @PathVariable Long reviewId) {
        reviewService.deleteReview(userId, reviewId);

        return ResponseEntity
                .noContent()
                .build();
    }

}
