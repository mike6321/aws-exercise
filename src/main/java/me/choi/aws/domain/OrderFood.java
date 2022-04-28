package me.choi.aws.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id")
@Table(name = "order_food")
@Entity
public class OrderFood {

    @Id
    @Column(name = "order_food_id")
    @GeneratedValue
    private Long orderFoodId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

}
