//package com.gracefullyugly.common.test;
//
//import com.gracefullyugly.domain.item.dto.ItemRequest;
//import com.gracefullyugly.domain.item.service.ItemService;
//import com.gracefullyugly.domain.user.dto.AdditionalRegRequest;
//import com.gracefullyugly.domain.user.dto.BasicRegRequest;
//import com.gracefullyugly.domain.user.dto.BasicRegResponse;
//import com.gracefullyugly.domain.user.entity.User;
//import com.gracefullyugly.domain.user.enumtype.Role;
//import com.gracefullyugly.domain.user.service.UserSearchService;
//import com.gracefullyugly.domain.user.service.UserService;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.annotation.Profile;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//@Profile("dev")
//@RequiredArgsConstructor
//@Component
//@Slf4j
//public class InitData {
//
//    private final UserService userService;
//    private final UserSearchService userSearchService;
//    private final ItemService itemService;
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void init() {
//        BasicRegRequest basicRegRequest = new BasicRegRequest(
//                "test",
//                "test"
//        );
//        BasicRegRequest basicRegRequestSeller = new BasicRegRequest(
//                "testSeller",
//                "testSeller"
//        );
//
//        // 일반 유저 (BUYER) 계정 생성
//        BasicRegResponse basicAccount = userService.createBasicAccount(basicRegRequest);
//
//        User findUser = userSearchService.findById(basicAccount.getUserId());
//
//        AdditionalRegRequest additionalRegRequest = AdditionalRegRequest.builder()
//                .role(Role.BUYER)
//                .nickname("testNickname")
//                .address("testAddress")
//                .build();
//
//        userService.completeRegistration(findUser.getId(), additionalRegRequest);
//
//        // 판매자 (SELLER) 계정 생성
//        BasicRegResponse basicAccountSeller = userService.createBasicAccount(basicRegRequestSeller);
//
//        User findSeller = userSearchService.findById(basicAccountSeller.getUserId());
//
//        AdditionalRegRequest additionalRegRequestSeller = AdditionalRegRequest.builder()
//                .role(Role.SELLER)
//                .nickname("testNicknameSeller")
//                .address("testSellerAddress")
//                .build();
//
//        userService.completeRegistration(findSeller.getId(), additionalRegRequestSeller);
//
//        // 상품 정보 생성
//        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();
//        itemService.save(findSeller.getId(), testItemData.get(0));
//        itemService.save(findSeller.getId(), testItemData.get(1));
//    }
//}
