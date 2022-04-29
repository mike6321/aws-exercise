package me.choi.aws.repository;

import lombok.RequiredArgsConstructor;
import me.choi.aws.domain.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager entityManager;

    public void save(Order order) {
        entityManager.persist(order);
    }

    public Order findOne(Long id ) {
        return entityManager.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        return entityManager.createQuery("select o from Order o join o.member m" +
                                                " where o.orderStatus = :status" +
                                                " and m.name like :name", Order.class)
                                                .setParameter("status", orderSearch.getOrderStatus())
                                                .setParameter("name", orderSearch.getMemberName())
                                                .setMaxResults(1000)
                                                .getResultList();
    }

}
