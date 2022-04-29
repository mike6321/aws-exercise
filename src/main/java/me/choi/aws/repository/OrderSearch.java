package me.choi.aws.repository;

import lombok.Getter;
import lombok.Setter;
import me.choi.aws.domain.OrderStatus;

@Getter @Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;

}
