package io.github.shygiants.sirenorder.utils;

import io.github.shygiants.sirenorder.domain.valueobject.CoffeeQuantity;
import jakarta.persistence.AttributeConverter;

public class CoffeeQuantityConverter implements AttributeConverter<CoffeeQuantity, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CoffeeQuantity coffeeQuantity) {
        return coffeeQuantity.getQuantity();
    }

    @Override
    public CoffeeQuantity convertToEntityAttribute(Integer integer) {
        return new CoffeeQuantity(integer);
    }
}
