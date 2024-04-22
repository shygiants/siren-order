package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.adapter.security.AuthenticatedMemberHolder;
import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.service.CafeService;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

class CafeControllerTest {
    Member requester;
    CafeController cafeController;
    CafeService cafeService;
    ControllerExceptionHandler controllerExceptionHandler;
    AuthenticatedMemberHolder authenticatedMemberHolder;

    @BeforeEach
    void setup() {
        requester = mock(Member.class);
        cafeService = mock(CafeService.class);
        authenticatedMemberHolder = mock(AuthenticatedMemberHolder.class);
        cafeController = new CafeController(cafeService, authenticatedMemberHolder);
        controllerExceptionHandler = new ControllerExceptionHandler();
    }

    @Test
    void testGetCafe() {
        when(cafeService.getCafe()).thenReturn(Cafe.fromId(CafeService.CAFE_ID));

        given()
            .standaloneSetup(cafeController)
            .contentType(MediaType.APPLICATION_JSON)
        .when()
            .get("/api/v1/cafe")
        .then()
            .status(HttpStatus.OK)
            .body("open", notNullValue());
        verify(cafeService).getCafe();
    }

    @Test
    void testGetCafeRuntimeException() {
        String message = "message";
        when(cafeService.getCafe()).thenThrow(new RuntimeException(message));

        given()
            .standaloneSetup(cafeController, controllerExceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
        .when()
            .get("/api/v1/cafe")
        .then()
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("msg", equalTo(message));
        verify(cafeService).getCafe();
    }

    @Test
    void testOpenCafe() {
        when(authenticatedMemberHolder.getMember()).thenReturn(requester);
        doNothing().when(cafeService).openCafe(any());

        given()
            .standaloneSetup(cafeController, controllerExceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
        .when()
            .post("/api/v1/cafe/open")
        .then()
            .status(HttpStatus.OK)
            .body(blankString());

        verify(authenticatedMemberHolder).getMember();
        verify(cafeService).openCafe(requester);
    }

    @Test
    void testOpenCafeNoAuthority() {
        String emailAddress = "test@example.com";
        when(requester.getEmailAddress()).thenReturn(new EmailAddress(emailAddress));
        when(authenticatedMemberHolder.getMember()).thenReturn(requester);
        doThrow(new CafeService.NoAuthorityException(requester)).when(cafeService).openCafe(any());

        given()
            .standaloneSetup(cafeController, controllerExceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
        .when()
            .post("/api/v1/cafe/open")
        .then()
            .status(HttpStatus.FORBIDDEN)
            .body("msg", containsString(emailAddress));

        verify(authenticatedMemberHolder).getMember();
        verify(cafeService).openCafe(requester);
        verify(requester).getEmailAddress();
    }

    @Test
    void testCloseCafe() {
        when(authenticatedMemberHolder.getMember()).thenReturn(requester);
        doNothing().when(cafeService).openCafe(any());

        given()
            .standaloneSetup(cafeController, controllerExceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
        .when()
            .post("/api/v1/cafe/close")
        .then()
            .status(HttpStatus.OK)
            .body(blankString());

        verify(authenticatedMemberHolder).getMember();
        verify(cafeService).closeCafe(requester);
    }

    @Test
    void testCloseCafeNoAuthority() {
        String emailAddress = "test@example.com";
        when(requester.getEmailAddress()).thenReturn(new EmailAddress(emailAddress));
        when(authenticatedMemberHolder.getMember()).thenReturn(requester);
        doThrow(new CafeService.NoAuthorityException(requester)).when(cafeService).closeCafe(any());

        given()
            .standaloneSetup(cafeController, controllerExceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
        .when()
            .post("/api/v1/cafe/close")
        .then()
            .status(HttpStatus.FORBIDDEN)
            .body("msg", containsString(emailAddress));

        verify(authenticatedMemberHolder).getMember();
        verify(cafeService).closeCafe(requester);
        verify(requester).getEmailAddress();
    }
}