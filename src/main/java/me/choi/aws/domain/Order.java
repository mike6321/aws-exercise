package me.choi.aws.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    private OrderStatus orderStatus;

    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public static Order createOrder(Member member, Delivery delivery, List<OrderItem> orderItems) {
        return Order.builder()
                    .member(member)
                    .delivery(delivery)
                    .orderItems(orderItems)
                    .orderStatus(OrderStatus.ORDER)
                    .orderDate(LocalDateTime.now())
                    .build();
    }

    public void cancel() {
        if (this.delivery.getDeliveryStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setOrderStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : this.orderItems) {
            orderItem.cancel();
        }
    }

    public int getTotalPrice() {
        return this.orderItems.stream()
                              .mapToInt(OrderItem::getTotalPrice)
                              .sum();
    }

}
