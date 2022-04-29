package me.choi.aws.service;

import me.choi.aws.domain.Address;
import me.choi.aws.domain.Member;
import me.choi.aws.domain.Order;
import me.choi.aws.domain.OrderStatus;
import me.choi.aws.domain.item.Book;
import me.choi.aws.exception.NotEnoughStockException;
import me.choi.aws.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired EntityManager entityManager;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();

        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order order = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, order.getOrderStatus());
        assertEquals(1, order.getOrderItems().size());
        assertEquals(10000 * orderCount, order.getTotalPrice());
        assertEquals(8, book.getStockQuantity());
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        int orderCount = 11;

        // when

        // then
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), book.getId(), orderCount));
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order order = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
        assertEquals(10, book.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        entityManager.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        entityManager.persist(member);
        return member;
    }

}
