package com.gracefullyugly.domain.order.service;

import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.order.dto.OrderItemDto;
import com.gracefullyugly.domain.order.dto.UpdateOrderAddressRequest;
import com.gracefullyugly.domain.order.dto.UpdateOrderPhoneNumberRequest;
import com.gracefullyugly.domain.order.entity.Order;
import com.gracefullyugly.domain.order.repository.OrderRepository;
import com.gracefullyugly.domain.orderitem.entity.OrderItem;
import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
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

    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        if (!userRepository.existsById(userId)) {
            return OrderResponse.builder()
                .message("회원 정보가 존재하지 않습니다.")
                .build();
        }

        Long orderId = orderRepository.save(new Order(userId, request.getAddress(), request.getPhoneNumber())).getId();
        orderItemRepository.saveAll(makeOrderItemList(orderId, request.getItemIdList()));

        return OrderResponse.builder()
            .message("주문이 정상적으로 저장되었습니다.")
            .build();
    }

    public OrderResponse updateOrderAddress(Long orderId, UpdateOrderAddressRequest request) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            return OrderResponse.builder()
                .message("주문 정보가 없습니다.")
                .build();
        }

        orderOptional.get().updateAddress(request.getAddress());

        return OrderResponse.builder()
            .message("주소가 정상적으로 변경되었습니다.")
            .build();
    }

    public OrderResponse updateOrderPhoneNumber(Long orderId, UpdateOrderPhoneNumberRequest request) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            return OrderResponse.builder()
                .message("주문 정보가 없습니다.")
                .build();
        }

        orderOptional.get().updatePhoneNumber(request.getPhoneNumber());

        return OrderResponse.builder()
            .message("연락처가 정상적으로 변경되었습니다.")
            .build();
    }

    /**
     * 주문하고자 하는 상품들 중 유효하지 않은 상품을 걸러낸 뒤, OrderItem 객체의 List를 만드는 메소드 입니다.
     */
    private List<OrderItem> makeOrderItemList(Long orderId, List<OrderItemDto> items) {
        return items.stream()
            .filter(item -> {
                Optional<Item> resultOptional = itemRepository.findById(item.getItemId());
                if (resultOptional.isEmpty()) {
                    return false;
                }

                Item result = resultOptional.get();

                return !result.isDeleted() && !result.getClosedDate().isBefore(LocalDateTime.now());
            })
            .map(item -> new OrderItem(item.getItemId(), orderId, item.getQuantity().intValue()))
            .toList();
    }
}
