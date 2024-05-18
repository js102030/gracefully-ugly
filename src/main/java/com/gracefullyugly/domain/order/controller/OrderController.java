package com.gracefullyugly.domain.order.controller;

import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.dto.OrderInfoResponse;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.order.dto.UpdateOrderAddressRequest;
import com.gracefullyugly.domain.order.dto.UpdateOrderPhoneNumberRequest;
import com.gracefullyugly.domain.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 주문 관리")
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private OrderService orderService;

    @Operation(summary = "상품 주문서 생성", description = "구매자가 구매할 상품의 주문서를 작성함")
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                     @Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "상품 주문서 정보 조회", description = "구매자가 주문한 상품의 주문서 정보를 조회함")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderInfoResponse> getOrderInfo(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                          @PathVariable("orderId") Long orderId) {
        OrderInfoResponse response = orderService.getOrderInfo(userId, orderId);

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "상품 주문 주소 수정", description = "구매자가 주문할 때 주문서에 입력한 주소를 수정함")
    @PutMapping("/orders/address/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderAddress(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                            @PathVariable("orderId") Long orderId,
                                                            @Valid @RequestBody UpdateOrderAddressRequest request) {
        OrderResponse response = orderService.updateAddress(userId, orderId, request);

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "상품 주문 연락처 수정", description = "구매자가 주문할 때 주문서에 입력한 연락처를 수정함")
    @PutMapping("/orders/phone_number/{orderId}")
    public ResponseEntity<OrderResponse> updatePhoneNumber(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                           @Valid @RequestBody UpdateOrderPhoneNumberRequest request,
                                                           @PathVariable("orderId") Long orderId) {
        OrderResponse response = orderService.updatePhoneNumber(userId, orderId, request);

        return ResponseEntity
                .ok(response);
    }
}
