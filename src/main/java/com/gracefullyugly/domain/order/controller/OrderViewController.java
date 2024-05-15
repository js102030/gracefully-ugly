package com.gracefullyugly.domain.order.controller;

import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.order.dto.OrderInfoResponse;
import com.gracefullyugly.domain.order.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class OrderViewController {

    private ItemSearchService itemSearchService;
    private OrderService orderService;

    @GetMapping("/orders/item/{itemId}")
    public String createOrderView(@Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
                                  @PathVariable("itemId") Long itemId,
                                  Model model) {
        Item item = itemSearchService.findById(itemId);
        model.addAttribute("Item", item);

        return "create-order";
    }

    @GetMapping("/orders/{ordersId}")
    public String getOrderInfo(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable("ordersId") Long orderId,
            Model model) {
        OrderInfoResponse response = orderService.getOrderInfo(userId, orderId);
        model.addAttribute("OrderInfoResponse", response);

        return "check-order";
    }

    @GetMapping("/orders/modify/{ordersId}")
    public String modifyOrderView(@Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
                                  @PathVariable("ordersId") Long orderId,
                                  Model model) {
        OrderInfoResponse response = orderService.getOrderInfo(userId, orderId);
        model.addAttribute("OrderInfoResponse", response);

        return "modify-order";
    }
}
