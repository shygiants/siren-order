package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.entity.Order;
import io.github.shygiants.sirenorder.domain.repository.OrderRepository;
import io.github.shygiants.sirenorder.domain.valueobject.CoffeeQuantity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CafeService cafeService;

    private void checkIfCafeIsOpen(Cafe cafe) {
        if (!cafe.getIsOpen()) {
            throw new CafeIsNotOpenException();
        }
    }

    public Long createOrder(Integer coffeeQuantityValue, Member customer) {
        CoffeeQuantity coffeeQuantity = new CoffeeQuantity(coffeeQuantityValue);

        Cafe cafe = cafeService.getCafe();
        checkIfCafeIsOpen(cafe);

        Order order = new Order(coffeeQuantity, cafe, customer);
        Order saved = orderRepository.save(order);

        return saved.getId();
    }

    public static class CafeIsNotOpenException extends IllegalStateException {
        public CafeIsNotOpenException() {
            super("Cafe is not open");
        }
    }


}
