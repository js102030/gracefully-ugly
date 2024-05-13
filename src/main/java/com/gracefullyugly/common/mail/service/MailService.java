package com.gracefullyugly.common.mail.service;

import com.gracefullyugly.domain.orderitem.dto.OrderItemInfo;
import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.service.UserSearchService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MailService {

    private final UserSearchService userSearchService;
    private final OrderItemRepository orderItemRepository;
    private final JavaMailSender emailSender;

    public void sendRefundMessage(Long userId, Long orderId) throws MessagingException, UnsupportedEncodingException {
        User findUser = userSearchService.findById(userId);
        if (findUser.isVerified()) {
            OrderItemInfo orderItemInfo = orderItemRepository.findOrderItemInfo(orderId);

            String email = findUser.getEmail();

            MimeMessage message = createRefundMessage(email, orderItemInfo);
            emailSender.send(message);
        }
    }

    private MimeMessage createRefundMessage(String to, OrderItemInfo orderItemInfo)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);
        message.setSubject("Gracefully-Ugly 환불 안내");

        String itemName = orderItemInfo.getItemName();
        String productionPlace = orderItemInfo.getProductionPlace();
        int itemPrice = orderItemInfo.getItemPrice();
        int itemQuantity = orderItemInfo.getOrderItemQuantity();
        int totalPrice = itemPrice * itemQuantity;
        String address = orderItemInfo.getAddress();
        String orderCreatedDate = orderItemInfo.getOrderCreatedDate();

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요</h1>";
        msgg += "<h1> 못난이 농산물 공동구매 Gracefully-Ugly 입니다.</h1>";
        msgg += "<br>";
        msgg += "<p>상품이 환불처리 되었습니다.<p>";
        msgg += "<br>";
        msgg += "<p>우리 농산물에 관심 주심에 감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>상품 환불 내역입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "상품 이름 : <strong>";
        msgg += itemName + "</strong><div><br/> ";
        msgg += "생산지 : <strong>";
        msgg += productionPlace + "</strong><div><br/> ";
        msgg += "환불 금액 : <strong>";
        msgg += totalPrice + "</strong><div><br/> ";
        msgg += "주소 : <strong>";
        msgg += address + "</strong><div><br/> ";
        msgg += "주문일 : <strong>";
        msgg += orderCreatedDate + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");

        message.setFrom(new InternetAddress("tltltlsl11@naver.com", "Gracefully-Ugly"));

        return message;
    }
}
