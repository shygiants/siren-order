package io.github.shygiants.sirenorder.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class CoffeeQuantity {

    private static final int MAX_QUANTITY = 10;
    private final int quantity;

    public CoffeeQuantity(int quantity) {
        this.quantity = quantity;
        validate();
    }

    private void validate() {
        if (quantity > MAX_QUANTITY) {
            String msg = String.format("Quantity exceeds maximum quantity %d: %d", MAX_QUANTITY, quantity);
            throw new IllegalArgumentException(msg);
        } else if (quantity <= 0) {
            String msg = String.format("Quantity must be greater than 0: %d", quantity);
            throw new IllegalArgumentException(msg);
        }
    }
}
