package me.choi.aws.service;

import lombok.RequiredArgsConstructor;
import me.choi.aws.domain.Delivery;
import me.choi.aws.domain.Member;
import me.choi.aws.domain.Order;
import me.choi.aws.domain.OrderItem;
import me.choi.aws.domain.item.Item;
import me.choi.aws.repository.ItemRepository;
import me.choi.aws.repository.MemberRepository;
import me.choi.aws.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, List.of(orderItem));

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    // TODO: 2022/04/29 검색 

}
