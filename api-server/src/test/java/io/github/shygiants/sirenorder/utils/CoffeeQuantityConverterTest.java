package io.github.shygiants.sirenorder.utils;

import io.github.shygiants.sirenorder.domain.valueobject.CoffeeQuantity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class CoffeeQuantityConverterTest {
    CoffeeQuantityConverter converter = new CoffeeQuantityConverter();

    @Test
    void testConvert() {
        // GIVEN
        CoffeeQuantity coffeeQuantity = new CoffeeQuantity(9);

        // WHEN
        Integer converted = converter.convertToDatabaseColumn(coffeeQuantity);
        CoffeeQuantity reconstructed = converter.convertToEntityAttribute(converted);

        // THEN
        assertThat(reconstructed).isEqualTo(coffeeQuantity);
    }
}