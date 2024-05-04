package com.gracefullyugly.domain.payment.service;

import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.payment.dto.KakaoPayApproveResponse;
import com.gracefullyugly.domain.payment.dto.KakaoPayReadyResponse;
import com.gracefullyugly.domain.payment.entity.Payment;
import com.gracefullyugly.domain.payment.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Transactional
public class PaymentService {

    private final static String KAKAO_PAY_READY_URL = "https://open-api.kakaopay.com/online/v1/payment/ready";
    private final static String KAKAO_PAY_APPROVE_URL = "https://open-api.kakaopay.com/online/v1/payment/approve";
    private final static String SECRET_KEY = "Secret key(dev)";
    private PaymentRepository paymentRepository;

    public String readyKakaoPay(OrderResponse paymentRequest) {
        KakaoPayReadyResponse response = postKakaoPayReady(paymentRequest);

        paymentRepository.save(new Payment(paymentRequest.getOrderId(), response.getTid(),
                paymentRequest.getItemInfoToPayment().getTotalAmount()));

        return response.getNext_redirect_pc_url();
    }

    // ToDo: 결제 승인 실패 시 예외 처리를 해야 함
    public KakaoPayApproveResponse approveKakaoPay(Long userId, Long orderId, String pgToken) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("결제 정보가 없습니다."));

        KakaoPayApproveResponse response = postKakaoPayApprove(userId, payment, pgToken);
        payment.updateIsPaid(true);

        return response;
    }

    private KakaoPayReadyResponse postKakaoPayReady(OrderResponse paymentRequest) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // Header 설정
        HttpHeaders headers = setHeader();
        // Params 설정
        MultiValueMap<String, String> params = setKakaoPayReadyParams(paymentRequest);
        // Body 설정
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        return restTemplate.postForObject(KAKAO_PAY_READY_URL, body, KakaoPayReadyResponse.class);
    }

    private HttpHeaders setHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + SECRET_KEY);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");

        return headers;
    }

    private MultiValueMap<String, String> setKakaoPayReadyParams(OrderResponse paymentRequest) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", paymentRequest.getOrderId().toString());
        params.add("partner_user_id", paymentRequest.getUserId().toString());
        params.add("item_name", paymentRequest.getItemInfoToPayment().getItemName());
        params.add("quantity", String.valueOf(paymentRequest.getItemInfoToPayment().getQuantity()));
        params.add("total_amount", String.valueOf(paymentRequest.getItemInfoToPayment().getTotalAmount()));
        params.add("tax_free_amount", String.valueOf(paymentRequest.getItemInfoToPayment().getTaxFreeAmount()));
        params.add("approval_url",
                "http://localhost:8080/api/payment/kakaopay/success/" + paymentRequest.getUserId() + "/"
                        + paymentRequest.getOrderId());
        params.add("cancel_url", "http://localhost:8080/");
        params.add("fail_url", "http://localhost:8080/");

        return params;
    }

    private KakaoPayApproveResponse postKakaoPayApprove(Long userId, Payment payment, String pgToken) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // Header 설정
        HttpHeaders headers = setHeader();
        // Params 설정
        MultiValueMap<String, String> params = setKakaoPayApproveParams(userId, payment, pgToken);
        // Body 설정
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        return restTemplate.postForObject(KAKAO_PAY_APPROVE_URL, body, KakaoPayApproveResponse.class);
    }

    private MultiValueMap<String, String> setKakaoPayApproveParams(Long userId, Payment payment, String pgToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", payment.getTid());
        params.add("partner_order_id", payment.getOrderId().toString());
        params.add("partner_user_id", userId.toString());
        params.add("pg_token", pgToken);

        return params;
    }
}
