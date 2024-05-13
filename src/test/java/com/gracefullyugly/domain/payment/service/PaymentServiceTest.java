//package com.gracefullyugly.domain.payment.service;
//
//import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
//import com.gracefullyugly.domain.groupbuy.service.GroupBuyService;
//import com.gracefullyugly.domain.groupbuyuser.repository.GroupBuyUserRepository;
//import com.gracefullyugly.domain.groupbuyuser.service.GroupBuyUserService;
//import com.gracefullyugly.domain.item.dto.ItemRequest;
//import com.gracefullyugly.domain.item.dto.ItemResponse;
//import com.gracefullyugly.domain.item.repository.ItemRepository;
//import com.gracefullyugly.domain.item.service.ItemService;
//import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
//import com.gracefullyugly.domain.order.dto.OrderResponse;
//import com.gracefullyugly.domain.order.repository.OrderRepository;
//import com.gracefullyugly.domain.order.service.OrderService;
//import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
//import com.gracefullyugly.domain.payment.entity.Payment;
//import com.gracefullyugly.domain.payment.repository.PaymentRepository;
//import com.gracefullyugly.domain.user.repository.UserRepository;
//import com.gracefullyugly.testutil.SetupDataUtils;
//import java.util.List;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//@Slf4j
//public class PaymentServiceTest {
//
//    @Autowired
//    PaymentRepository paymentRepository;
//
//    @Autowired
//    PaymentService paymentService;
//
//    @Autowired
//    GroupBuyUserRepository groupBuyUserRepository;
//
//    @Autowired
//    GroupBuyUserService groupBuyUserService;
//
//    @Autowired
//    GroupBuyRepository groupBuyRepository;
//
//    @Autowired
//    GroupBuyService groupBuyService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    ItemRepository itemRepository;
//
//    @Autowired
//    BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    ItemService itemService;
//
//    @Autowired
//    OrderService orderService;
//
//    @Autowired
//    OrderRepository orderRepository;
//
//    @Autowired
//    OrderItemRepository orderItemRepository;
//
//    private Long groupBuyId;
//
//    private Long userId;
//
//    private ItemResponse firstItem;
//
//    private ItemResponse secondItem;
//
//    private OrderResponse orderResponse;
//
//    @BeforeEach
//    void setupTestData() {
//        // 회원 정보 세팅
//        userId = userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder)).getId();
//        userRepository.save(
//                SetupDataUtils.makeCustomTestUser(null, "customUser", "customUser", "customUser", "custom@custom.com",
//                        "customAddress", passwordEncoder));
//        userRepository.save(SetupDataUtils.makeTestAdmin(passwordEncoder));
//
//        // 상품 정보 세팅
//        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();
//
//        firstItem = itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
//                testItemData.get(0));
//        secondItem = itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
//                testItemData.get(1));
//
//        // 주문 정보 세팅
//        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemRepository.findAll());
//        orderResponse = orderService.createOrder(userId, testRequest);
//    }
//
//    @AfterEach
//    void deleteTestData() {
//        userRepository.deleteAll();
//        itemRepository.deleteAll();
//        orderRepository.deleteAll();
//        orderItemRepository.deleteAll();
//        groupBuyRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("결제 준비 단게 메소드 테스트")
//    void readyKakaoPayTest() {
//        // WHEN
//        String response = paymentService.readyKakaoPay(orderResponse);
//
//        // THEN
//        Payment payment = paymentRepository.findByOrderId(orderResponse.getOrderId()).get();
//        assertThat(payment).isNotNull();
//
//        assertThat(payment.getOrderId()).isEqualTo(orderResponse.getOrderId());
//        assertThat(payment.getTotalPrice()).isEqualTo(orderResponse.getItemInfoToPayment().getTotalAmount());
//        assertThat(payment.isPaid()).isFalse();
//        assertThat(payment.isRefunded()).isFalse();
//        assertThat(response).containsPattern("https:[/][/]online-pay.kakao.com[/]mockup[/]v1[/][A-z0-9]*[/]info");
//    }
//}
