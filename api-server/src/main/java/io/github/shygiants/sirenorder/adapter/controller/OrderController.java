package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.adapter.controller.exceptions.BadRequestException;
import io.github.shygiants.sirenorder.adapter.controller.exceptions.ForbiddenException;
import io.github.shygiants.sirenorder.adapter.security.AuthenticatedMemberHolder;
import io.github.shygiants.sirenorder.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthenticatedMemberHolder authenticatedMemberHolder;

    @PostMapping("/api/v1/orders")
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Long createdOrderId = orderService.createOrder(request.coffeeQuantity, authenticatedMemberHolder.getMember());
            return new CreateOrderResponse(createdOrderId);
        } catch (OrderService.CafeIsNotOpenException e) {
            throw new ForbiddenException(e);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e);
        }
    }

    public record CreateOrderRequest(Integer coffeeQuantity) {

    }

    public record CreateOrderResponse(Long id) {

    }
}
