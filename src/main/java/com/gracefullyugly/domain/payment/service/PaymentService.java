package com.gracefullyugly.domain.payment.service;

import com.gracefullyugly.common.exception.custom.ExistException;
import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.common.mail.service.MailService;
import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import com.gracefullyugly.domain.groupbuyuser.repository.GroupBuyUserRepository;
import com.gracefullyugly.domain.groupbuyuser.service.GroupBuyUserService;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.orderitem.dto.ItemOrderDetails;
import com.gracefullyugly.domain.orderitem.entity.OrderItem;
import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
import com.gracefullyugly.domain.payment.dto.KakaoPayApproveResponse;
import com.gracefullyugly.domain.payment.dto.KakaoPayReadyResponse;
import com.gracefullyugly.domain.payment.entity.Payment;
import com.gracefullyugly.domain.payment.repository.PaymentRepository;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PaymentService {

    private final static String KAKAO_PAY_READY_URL = "https://open-api.kakaopay.com/online/v1/payment/ready";
    private final static String KAKAO_PAY_APPROVE_URL = "https://open-api.kakaopay.com/online/v1/payment/approve";
    private final static String KAKAO_PAY_CANCEL_URL = "https://open-api.kakaopay.com/online/v1/payment/cancel";
    private final static String SECRET_KEY = "DEV0E39D58BA039991E20FB24D32905C6B1B0648";
    private PaymentRepository paymentRepository;
    private OrderItemRepository orderItemRepository;
    private GroupBuyRepository groupBuyRepository;
    private GroupBuyUserRepository groupBuyUserRepository;
    private GroupBuyUserService groupBuyUserService;
    private MailService mailService;

    public String readyKakaoPay(OrderResponse paymentRequest) {
        KakaoPayReadyResponse response = postKakaoPayReady(paymentRequest);

        paymentRepository.save(new Payment(paymentRequest.getOrderId(), response.getTid(),
                paymentRequest.getItemInfoToPayment().getTotalAmount()));

        log.error("{}", response.getNext_redirect_pc_url());

        return response.getNext_redirect_pc_url();
    }

    public KakaoPayApproveResponse approveKakaoPay(Long userId, Long orderId, String pgToken) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("결제 정보가 없습니다."));

        KakaoPayApproveResponse response = postKakaoPayApprove(userId, payment, pgToken);
        payment.updateIsPaid(true);

        List<ItemOrderDetails> itemOrderDetails = orderItemRepository.findItemsAndQuantityByOrderId(orderId);
        itemOrderDetails.forEach(
                itemOrderDetail -> itemOrderDetail.getItem().decreaseStock(itemOrderDetail.getQuantity()));

        List<OrderItem> orderItemList = orderItemRepository.findAllByOrdersId(orderId);
        groupBuyUserService.joinGroupBuy(userId, orderItemList);

        return response;
    }

    public void refundKakaoPay(Long userId, Long orderId) throws MessagingException, UnsupportedEncodingException {
        Payment payment = paymentRepository.findPaymentByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("결제 정보가 없습니다."));

        if (!groupBuyRepository.findCompletedGroupBuyByOrderId(orderId).isEmpty()) {
            throw new ExistException("완료된 공동 구매는 환불이 불가합니다.");
        }

        postKakaoPayRefund(payment);
        mailService.sendRefundMessage(userId, orderId);
        payment.updateIsPaid(false);
        payment.updateIsRefunded(true);

        List<ItemOrderDetails> itemOrderDetails = orderItemRepository.findItemsAndQuantityByOrderId(orderId);
        itemOrderDetails.forEach(
                itemOrderDetail -> itemOrderDetail.getItem().increaseStock(itemOrderDetail.getQuantity()));

        groupBuyUserRepository.deleteAllByUserIdAndOrderId(userId, orderId);
    }

    /**
     * 카카오 결제 준비 단계 API를 호출해 Response를 받아오는 메소드 입니다. 결제 준비 단계 도중 문제 발생 시 RestClientResponseException을 던집니다.
     */
    private KakaoPayReadyResponse postKakaoPayReady(OrderResponse paymentRequest) throws RestClientResponseException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // Header 설정
        HttpHeaders headers = setHeader();
        // Params 설정
        Map<String, String> params = setKakaoPayReadyParams(paymentRequest);
        // Body 설정
        HttpEntity<Map<String, String>> body = new HttpEntity<>(params, headers);

        return restTemplate.postForObject(KAKAO_PAY_READY_URL, body, KakaoPayReadyResponse.class);
    }

    /**
     * 카카오 결제 승인 단계 API를 호출해 Response를 받아오는 메소드 입니다. 결제 승인 단계 도중 문제 발생 시 RestClientResponseException을 던집니다.
     */
    private KakaoPayApproveResponse postKakaoPayApprove(Long userId, Payment payment, String pgToken)
            throws RestClientResponseException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // Header 설정
        HttpHeaders headers = setHeader();
        // Params 설정
        Map<String, String> params = setKakaoPayApproveParams(userId, payment, pgToken);
        // Body 설정
        HttpEntity<Map<String, String>> body = new HttpEntity<>(params, headers);

        return restTemplate.postForObject(KAKAO_PAY_APPROVE_URL, body, KakaoPayApproveResponse.class);
    }

    /**
     * 카카오 결제 환불 단계 API를 호출해 Response를 받아오는 메소드 입니다. 결제 환불 단계 도중 문제 발생 시 RestClientResponseException을 던집니다.
     */
    private void postKakaoPayRefund(Payment payment) throws RestClientResponseException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // Header 설정
        HttpHeaders headers = setHeader();
        // Params 설정
        Map<String, String> params = setKakaoPayRefundParams(payment);
        // Body 설정
        HttpEntity<Map<String, String>> body = new HttpEntity<>(params, headers);

        restTemplate.postForObject(KAKAO_PAY_CANCEL_URL, body, String.class);
    }

    /**
     * 카카오 결제 API 호출을 위한 Header 정보를 세팅하는 메소드 입니다.
     */
    private HttpHeaders setHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + SECRET_KEY);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");

        return headers;
    }

    /**
     * 카카오 결제 준비 단계에 필요한 파라메터를 세팅하는 메소드 입니다.
     */
    private Map<String, String> setKakaoPayReadyParams(OrderResponse paymentRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("partner_order_id", paymentRequest.getOrderId().toString());
        params.put("partner_user_id", paymentRequest.getUserId().toString());
        params.put("item_name", paymentRequest.getItemInfoToPayment().getItemName());
        params.put("quantity", String.valueOf(paymentRequest.getItemInfoToPayment().getQuantity()));
        params.put("total_amount", String.valueOf(paymentRequest.getItemInfoToPayment().getTotalAmount()));
        params.put("tax_free_amount", String.valueOf(paymentRequest.getItemInfoToPayment().getTaxFreeAmount()));
        params.put("approval_url",
                "/payment/kakaopay/success/" + paymentRequest.getUserId() + "/"
                        + paymentRequest.getOrderId());
        params.put("cancel_url", "/payment/cancel?orderId=" + paymentRequest.getOrderId());
        params.put("fail_url", "/payment/fail?orderId=" + paymentRequest.getOrderId());

        return params;
    }

    /**
     * 카카오 결제 승인 단계에 필요한 파라메터를 세팅하는 메소드 입니다.
     */
    private Map<String, String> setKakaoPayApproveParams(Long userId, Payment payment, String pgToken) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("tid", payment.getTid());
        params.put("partner_order_id", payment.getOrderId().toString());
        params.put("partner_user_id", userId.toString());
        params.put("pg_token", pgToken);

        return params;
    }

    /**
     * 카카오 결제 환불 단계에 필요한 파라메터를 세팅하는 메소드 입니다.
     */
    private Map<String, String> setKakaoPayRefundParams(Payment payment) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME");
        params.put("tid", payment.getTid());
        params.put("cancel_amount", String.valueOf(payment.getTotalPrice()));
        params.put("cancel_tax_free_amount", String.valueOf(payment.getTotalPrice()));

        return params;
    }
}
