package io.github.shygiants.sirenorder.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CoffeeQuantityTest {

    @Test
    void testCoffeeQuantity() {
        // GIVEN
        int validCoffeeQuantity = 1;

        // WHEN
        CoffeeQuantity coffeeQuantity = new CoffeeQuantity(validCoffeeQuantity);

        // THEN
        assertThat(coffeeQuantity.getQuantity()).isEqualTo(validCoffeeQuantity);
    }

    @Test
    void testInvalidCoffeeQuantityMinusValue() {
        // GIVEN
        int invalidCoffeeQuantity = -1;

        // WHEN
        Throwable throwable = catchThrowable(() -> new CoffeeQuantity(invalidCoffeeQuantity));

        // THEN
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.valueOf(invalidCoffeeQuantity));
    }

    @Test
    void testInvalidCoffeeQuantityZeroValue() {
        // GIVEN
        int invalidCoffeeQuantity = 0;

        // WHEN
        Throwable throwable = catchThrowable(() -> new CoffeeQuantity(invalidCoffeeQuantity));

        // THEN
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.valueOf(invalidCoffeeQuantity));
    }

    @Test
    void testInvalidCoffeeQuantityTooHighValue() {
        // GIVEN
        int invalidCoffeeQuantity = 11;

        // WHEN
        Throwable throwable = catchThrowable(() -> new CoffeeQuantity(invalidCoffeeQuantity));

        // THEN
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.valueOf(invalidCoffeeQuantity));
    }
}