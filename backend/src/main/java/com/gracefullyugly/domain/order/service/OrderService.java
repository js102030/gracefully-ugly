package com.gracefullyugly.domain.order.service;

import com.gracefullyugly.common.exception.custom.ForbiddenException;
import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.dto.OrderInfoResponse;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.order.dto.OrderItemDto;
import com.gracefullyugly.domain.order.dto.UpdateOrderAddressRequest;
import com.gracefullyugly.domain.order.dto.UpdateOrderPhoneNumberRequest;
import com.gracefullyugly.domain.order.entity.Order;
import com.gracefullyugly.domain.order.repository.OrderRepository;
import com.gracefullyugly.domain.orderitem.dto.ItemInfoToPaymentDto;
import com.gracefullyugly.domain.orderitem.dto.OrderItemInfoResponse;
import com.gracefullyugly.domain.orderitem.entity.OrderItem;
import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
import com.gracefullyugly.domain.payment.entity.Payment;
import com.gracefullyugly.domain.payment.repository.PaymentRepository;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.Role;
import com.gracefullyugly.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class OrderService {

    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private PaymentRepository paymentRepository;

    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Order order = orderRepository.save(new Order(userId, request.getAddress(), request.getPhoneNumber()));

        ItemInfoToPaymentDto itemInfoToPaymentDto = new ItemInfoToPaymentDto();
        List<OrderItem> orderItemList = makeOrderItemList(order.getId(), request.getItemIdList(),
                itemInfoToPaymentDto);

        if (orderItemList.isEmpty()) {
            throw new NotFoundException("주문 가능한 상품이 없습니다.");
        }

        orderItemRepository.saveAll(orderItemList);

        if (orderItemList.size() != 1) {
            itemInfoToPaymentDto.setItemName(
                    itemInfoToPaymentDto.getItemName() + " 외 " + (orderItemList.size() - 1) + "종");
        }

        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .address(order.getAddress())
                .phoneNumber(order.getPhoneNumber())
                .ItemInfoToPayment(itemInfoToPaymentDto)
                .build();
    }

    public OrderInfoResponse getOrderInfo(Long userId, Long orderId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new NotFoundException("주문 정보가 없습니다.");
        }

        Order order = orderOptional.get();
        String nickname = userOptional.get().getNickname();
        Payment payment = paymentRepository.findByOrderId(orderId).orElse(null);
        List<OrderItemInfoResponse> orderItemList = orderItemRepository.findAllByOrderId(orderId);

        return OrderInfoResponse.builder()
                .order(order)
                .nickname(nickname)
                .payment(payment)
                .orderItemList(orderItemList)
                .build();
    }

    public OrderResponse updateOrderAddress(Long userId, Role role, Long orderId, UpdateOrderAddressRequest request) {
        Order order = returnOrder(userId, role, orderId);
        order.updateAddress(request.getAddress());

        return OrderResponse.builder()
                .orderId(order.getId())
                .address(order.getAddress())
                .build();
    }

    public OrderResponse updateOrderPhoneNumber(Long userId, Role role, Long orderId,
                                                UpdateOrderPhoneNumberRequest request) {
        Order order = returnOrder(userId, role, orderId);
        order.updatePhoneNumber(request.getPhoneNumber());

        return OrderResponse.builder()
                .orderId(order.getId())
                .phoneNumber(order.getPhoneNumber())
                .build();
    }

    /**
     * 주문하고자 하는 상품들 중 유효하지 않은 상품을 걸러낸 뒤, OrderItem 객체의 List를 만드는 메소드 입니다. orderItemInfoDto는 결제 프로세스에 필요한 정보를 저장하기 위한
     * 파라메터입니다.
     */
    private List<OrderItem> makeOrderItemList(Long orderId, List<OrderItemDto> items,
                                              ItemInfoToPaymentDto itemInfoToPaymentDto) {
        return items.stream()
                .filter(item -> {
                    Optional<Item> resultOptional = itemRepository.findById(item.getItemId());
                    if (resultOptional.isEmpty()) {
                        return false;
                    }

                    Item result = resultOptional.get();

                    if (!result.isDeleted() && !result.getClosedDate().isBefore(LocalDateTime.now())) {
                        itemInfoToPaymentDto.setFirstItemName(result.getName())
                                .addQuantity(item.getQuantity().intValue() / result.getTotalSalesUnit())
                                .addTotalAmount(result.getPrice() * (item.getQuantity().intValue()
                                        / result.getTotalSalesUnit()));
                        return true;
                    }

                    return false;
                })
                .map(item -> new OrderItem(item.getItemId(), orderId, item.getQuantity().intValue()))
                .toList();
    }

    /**
     * 유효성 검사 이후 Order 객체를 반환하는 메소드입니다.
     */
    private Order returnOrder(Long userId, Role role, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("주문 정보가 없습니다."));

        if (!order.getUserId().equals(userId) && !role.equals(Role.ADMIN)) {
            throw new ForbiddenException("접근 권한이 없습니다.");
        }

        return order;
    }
}
