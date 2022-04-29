package me.choi.aws.domain;

import lombok.*;
import me.choi.aws.domain.item.Item;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    public void cancel() {
        this.getItem().addStock(this.count);
    }

    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }

    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItemBuilder builder = OrderItem.builder()
                                            .item(item)
                                            .orderPrice(orderPrice)
                                            .count(count);

        builder.item.removeStock(count);

        return builder.build();
    }

}
