package io.github.shygiants.sirenorder.domain.entity;

import io.github.shygiants.sirenorder.domain.enumerate.OrderState;
import io.github.shygiants.sirenorder.domain.valueobject.CoffeeQuantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class OrderTest {
    Cafe cafe;
    Member customer;

    @BeforeEach
    void setup() {
        cafe = mock(Cafe.class);
        customer = mock(Member.class);
    }

    @Test
    void testCreate() {
        // GIVEN
        CoffeeQuantity coffeeQuantity = new CoffeeQuantity(1);

        // WHEN
        Order order = new Order(coffeeQuantity, cafe, customer);

        // THEN
        assertThat(order.getCoffeeQuantity()).isEqualTo(coffeeQuantity);
        assertThat(order.getCafe()).isEqualTo(cafe);
        assertThat(order.getCustomer()).isEqualTo(customer);
        assertThat(order.getState()).isEqualTo(OrderState.PENDING);
    }
}