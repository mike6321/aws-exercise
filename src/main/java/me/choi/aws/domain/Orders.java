package me.choi.aws.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = "id")
@Table(name = "orders")
@Entity
public class Orders {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "estimate_time")
    private Integer estimateTime = 1;

    @Column(name = "deliver_finish")
    private Integer deliverFinish = 0;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

}
