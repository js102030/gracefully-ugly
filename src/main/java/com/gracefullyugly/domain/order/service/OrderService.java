package com.gracefullyugly.domain.order.service;

import com.gracefullyugly.common.exception.custom.ForbiddenException;
import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.dto.OrderDtoUtil;
import com.gracefullyugly.domain.order.dto.OrderInfoResponse;
import com.gracefullyugly.domain.order.dto.OrderItemDto;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.order.dto.UpdateOrderAddressRequest;
import com.gracefullyugly.domain.order.dto.UpdateOrderPhoneNumberRequest;
import com.gracefullyugly.domain.order.entity.Order;
import com.gracefullyugly.domain.order.repository.OrderRepository;
import com.gracefullyugly.domain.orderitem.dto.OrderItemInfoResponse;
import com.gracefullyugly.domain.orderitem.entity.OrderItem;
import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
import com.gracefullyugly.domain.payment.entity.Payment;
import com.gracefullyugly.domain.payment.repository.PaymentRepository;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;

    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        Order order = new Order(userId, request.getAddress(), request.getPhoneNumber());
        orderRepository.save(order);

        List<Long> itemIds = request.getOrderItems().stream()
                .map(OrderItemDto::getItemId)
                .toList();

        List<Item> validItems = itemRepository.findValidItemsByIds(itemIds);
        Map<Long, Item> itemMap = validItems.stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : request.getOrderItems()) {
            if (!itemMap.containsKey(orderItemDto.getItemId())) {
                throw new NotFoundException("주문할 수 없는 상품이 포함되어 있습니다.");
            }
            orderItems.add(new OrderItem(
                    orderItemDto.getItemId(),
                    order.getId(),
                    orderItemDto.getQuantity()
            ));
        }

        orderItemRepository.saveAll(orderItems);
        
        int totalPrice = calculateTotalPrice(orderItems, itemMap);

        return OrderDtoUtil.ordertoOrderResponse(order, totalPrice);
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
        List<OrderItemInfoResponse> orderItemList = orderItemRepository.getOrderItemListByOrderId(orderId);

        return OrderInfoResponse.builder()
                .order(order)
                .nickname(nickname)
                .payment(payment)
                .orderItemList(orderItemList)
                .build();
    }

    public OrderResponse updateAddress(Long userId, Long orderId, UpdateOrderAddressRequest request) {
        Order order = findValidOrder(userId, orderId);
        order.updateAddress(request.getAddress());

        return OrderResponse.builder()
                .orderId(order.getId())
                .address(order.getAddress())
                .build();
    }

    public OrderResponse updatePhoneNumber(Long userId, Long orderId,
                                           UpdateOrderPhoneNumberRequest request) {
        Order order = findValidOrder(userId, orderId);
        order.updatePhoneNumber(request.getPhoneNumber());

        return OrderResponse.builder()
                .orderId(order.getId())
                .phoneNumber(order.getPhoneNumber())
                .build();
    }

    private int calculateTotalPrice(List<OrderItem> orderItems, Map<Long, Item> itemMap) {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            Item item = itemMap.get(orderItem.getItemId());
            totalPrice += item.getPrice() * orderItem.getQuantity();
        }
        return totalPrice;
    }

    private Order findValidOrder(Long userId, Long orderId) {
        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("주문 정보가 없습니다."));

        if (!findOrder.getUserId().equals(userId)) {
            throw new ForbiddenException("접근 권한이 없습니다.");
        }

        return findOrder;
    }
}
