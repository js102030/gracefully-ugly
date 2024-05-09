package com.gracefullyugly.common.controller;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.review.dto.ReviewResponse;
import com.gracefullyugly.domain.review.service.ReviewSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class CommonController {

    private final ItemSearchService itemSearchService;
    private final ReviewSearchService reviewSearchService;

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

//    @GetMapping("/check-order")
//    public String checkOrder() {
//        return "complete-payment";
//    }

//    @GetMapping("/create-order")
//    public String createOrder() {
//        return "create-order";
//    }

    @GetMapping("/create-review")
    public String createReview() {
        return "create-review";
    }

    @GetMapping("/group-buying")
    public String groupBuying(@RequestParam("itemId") Long itemId, Model model) {
        List<ReviewResponse> reviewResponse = reviewSearchService.getReviewsOrEmptyByItemId(itemId);
        ItemResponse itemResponse = itemSearchService.findOneItem(itemId);
        model.addAttribute("reviews", reviewResponse); // 리뷰 데이터를 모델에 추가
        model.addAttribute("item", itemResponse);
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

    @GetMapping("/log")
    public String log() {
        return "login";
    }

    @PostMapping("/login")
    public String login() {
        return "main";
    }

    @GetMapping("/cart-list")
    public String cart_list() {
        return "cart-list";
    }

    @GetMapping("/modify-order")
    public String modify_order() {
        return "modify-order";
    }

    @GetMapping("/purchase_history")
    public String purchase_history() {
        return "purchase_history";
    }

}
