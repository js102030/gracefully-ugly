package com.gracefullyugly.domain.order.controller;

import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.dto.OrderInfoResponse;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.order.dto.UpdateOrderAddressRequest;
import com.gracefullyugly.domain.order.dto.UpdateOrderPhoneNumberRequest;
import com.gracefullyugly.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private OrderService orderService;

    @PostMapping("/orders/{userId}")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable("userId") Long userId, @Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(userId, request));
    }

    @GetMapping("/orders/{userId}/{ordersId}")
    public ResponseEntity<OrderInfoResponse> getOrderInfo(@PathVariable("userId") Long userId, @PathVariable("ordersId") Long orderId) {
        return ResponseEntity.ok(orderService.getOrderInfo(userId, orderId));
    }

    @PutMapping("/orders/address/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderAddress(@PathVariable("orderId") Long orderId, @Valid @RequestBody UpdateOrderAddressRequest request) {
        return ResponseEntity.ok(orderService.updateOrderAddress(orderId, request));
    }

    @PutMapping("/orders/phone_number/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderPhoneNumber(@PathVariable("orderId") Long orderId, @Valid @RequestBody UpdateOrderPhoneNumberRequest request) {
        return ResponseEntity.ok(orderService.updateOrderPhoneNumber(orderId, request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<OrderResponse> notFoundInfoHandler(IllegalArgumentException err) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(OrderResponse.builder()
            .message(err.getLocalizedMessage())
            .build());
    }
}
