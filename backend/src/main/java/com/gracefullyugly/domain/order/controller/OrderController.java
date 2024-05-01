package com.gracefullyugly.domain.order.controller;

import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.order.dto.UpdateOrderAddressRequest;
import com.gracefullyugly.domain.order.dto.UpdateOrderPhoneNumberRequest;
import com.gracefullyugly.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(orderService.createOrder(userId, request));
    }

    @PutMapping("/orders/address/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderAddress(@PathVariable("orderId") Long orderId, @Valid @RequestBody UpdateOrderAddressRequest request) {
        return ResponseEntity.ok(orderService.updateOrderAddress(orderId, request));
    }

    @PutMapping("/orders/phone_number/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderPhoneNumber(@PathVariable("orderId") Long orderId, @Valid @RequestBody UpdateOrderPhoneNumberRequest request) {
        return ResponseEntity.ok(orderService.updateOrderPhoneNumber(orderId, request));
    }
}
