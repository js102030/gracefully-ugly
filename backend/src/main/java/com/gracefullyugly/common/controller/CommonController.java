package com.gracefullyugly.common.controller;

import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final ItemSearchService itemSearchService;

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    @GetMapping("/join2")
    public String join2Page() {
        return "join2";
    }

    @GetMapping("/my-page")
    public String myPagePage() {
        return "my-page";
    }

    @GetMapping("/salesPost")
    public String sales_post() {
        return "salesPost";
    }

    @GetMapping("/sellerList")
    public String sellerList() {
        return "sellerList";
    }

    @GetMapping("/sellerDetails")
    public String sellerDetails() {
        return "sellerDetails";
    }

    @GetMapping("/check-order")
    public String checkOrder() {
        return "check-order";
    }

    @GetMapping("/create-order")
    public String createOrder() {
        return "create-order";
    }

    @GetMapping("/create-review")
    public String createReview() {
        return "create-review";
    }

    @GetMapping("/group-buying")
    public String groupBuying() {
        return "group-buying";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/admin-report")
    public String adminReport() {
        return "admin-report";
    }

    @GetMapping("/login.html")
    public String login() {
        return "login.html";
    }

    @GetMapping("/mainAfter")
    public String mainAfterPage() {
        return "mainAfter";
    }

    @GetMapping("/cart-list")
    public String cartListPage() {
        return "cart-list";
    }

    @GetMapping("/purchase_history")
    public String PurchaseHistory() {
        return "purchase_history";
    }
}
