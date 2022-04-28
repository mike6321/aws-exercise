package me.choi.aws.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id")
@Table(name = "menu")
@Entity
public class Menu {

    @Id
    @GeneratedValue
    @Column(name = "menu_id")
    private Long id;

    @Column(name = "food_name")
    private String foodName;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shops;

}
