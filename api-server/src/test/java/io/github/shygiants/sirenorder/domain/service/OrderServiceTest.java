package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.entity.Order;
import io.github.shygiants.sirenorder.domain.repository.OrderRepository;
import io.github.shygiants.sirenorder.domain.valueobject.CoffeeQuantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    static final Long ORDER_ID = 1L;

    Cafe cafe;
    Member customer;
    OrderRepository orderRepository;
    CafeService cafeService;
    OrderService orderService;

    @BeforeEach
    void setup() {
        cafe = mock(Cafe.class);
        customer = mock(Member.class);
        orderRepository = mock(OrderRepository.class);
        cafeService = mock(CafeService.class);
        orderService = new OrderService(orderRepository, cafeService);
    }

    @Test
    void testCreateOrder() {
        // GIVEN
        when(cafe.getIsOpen()).thenReturn(true);
        when(cafeService.getCafe()).thenReturn(cafe);
        when(orderRepository.save(any(Order.class))).then(invocationOnMock -> {
            Order order = spy(invocationOnMock.getArgument(0, Order.class));
            when(order.getId()).thenReturn(ORDER_ID);
            return order;
        });
        int coffeeQuantity = 1;

        // WHEN
        Long orderId = orderService.createOrder(coffeeQuantity, customer);

        // THEN
        verify(cafeService).getCafe();
        verify(cafe).getIsOpen();
        verify(orderRepository).save(argThat(order -> order.equals(
                new Order(new CoffeeQuantity(coffeeQuantity), cafe, customer))));
        assertThat(orderId).isEqualTo(ORDER_ID);
    }

    @Test
    void testCreateOrderCafeNotOpen() {
        // GIVEN
        when(cafe.getIsOpen()).thenReturn(false);
        when(cafeService.getCafe()).thenReturn(cafe);
        when(orderRepository.save(any(Order.class))).then(invocationOnMock -> {
            Order order = spy(invocationOnMock.getArgument(0, Order.class));
            when(order.getId()).thenReturn(ORDER_ID);
            return order;
        });
        int coffeeQuantity = 1;

        // WHEN
        Throwable throwable = catchThrowable(() -> orderService.createOrder(coffeeQuantity, customer));

        // THEN
        verify(cafeService).getCafe();
        verify(cafe).getIsOpen();
        verify(orderRepository, never()).save(any());
        assertThat(throwable).isInstanceOf(OrderService.CafeIsNotOpenException.class);
    }
}