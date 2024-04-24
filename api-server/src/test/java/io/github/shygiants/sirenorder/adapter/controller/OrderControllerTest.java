package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.adapter.security.AuthenticatedMemberHolder;
import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.service.CafeService;
import io.github.shygiants.sirenorder.domain.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


class OrderControllerTest {
    static final Long ORDER_ID = 1L;
    Member customer;
    AuthenticatedMemberHolder authenticatedMemberHolder;
    OrderService orderService;
    OrderController orderController;
    ControllerExceptionHandler controllerExceptionHandler;

    @BeforeEach
    void setup() {
        customer = mock(Member.class);
        authenticatedMemberHolder = mock(AuthenticatedMemberHolder.class);
        orderService = mock(OrderService.class);
        orderController = new OrderController(orderService, authenticatedMemberHolder);
        controllerExceptionHandler = new ControllerExceptionHandler();
    }

    @Test
    void testCreateOrder() {
        when(orderService.createOrder(anyInt(), any(Member.class))).thenReturn(ORDER_ID);
        when(authenticatedMemberHolder.getMember()).thenReturn(customer);
        Integer coffeeQuantity = 2;

        given()
            .standaloneSetup(orderController, controllerExceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new OrderController.CreateOrderRequest(coffeeQuantity))
        .when()
            .post("/api/v1/orders")
        .then()
            .status(HttpStatus.OK)
            .body("id", equalTo(ORDER_ID.intValue()));

        verify(orderService).createOrder(coffeeQuantity, customer);
        verify(authenticatedMemberHolder).getMember();
    }

    @Test
    void testCreateOrderInvalidCoffeeQuantity() {
        when(orderService.createOrder(anyInt(), any(Member.class))).thenThrow(
                new IllegalArgumentException("invalid coffee quantity"));
        when(authenticatedMemberHolder.getMember()).thenReturn(customer);
        Integer coffeeQuantity = 11;

        given()
            .standaloneSetup(orderController, controllerExceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new OrderController.CreateOrderRequest(coffeeQuantity))
        .when()
            .post("/api/v1/orders")
        .then()
            .status(HttpStatus.BAD_REQUEST)
            .body("msg", notNullValue());

        verify(orderService).createOrder(coffeeQuantity, customer);
        verify(authenticatedMemberHolder).getMember();
    }

    @Test
    void testCreateOrderCafeNotOpen() {
        when(orderService.createOrder(anyInt(), any(Member.class))).thenThrow(
                new OrderService.CafeIsNotOpenException());
        when(authenticatedMemberHolder.getMember()).thenReturn(customer);
        Integer coffeeQuantity = 1;

        given()
            .standaloneSetup(orderController, controllerExceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new OrderController.CreateOrderRequest(coffeeQuantity))
        .when()
            .post("/api/v1/orders")
        .then()
            .status(HttpStatus.FORBIDDEN)
            .body("msg", notNullValue());

        verify(orderService).createOrder(coffeeQuantity, customer);
        verify(authenticatedMemberHolder).getMember();
    }
}