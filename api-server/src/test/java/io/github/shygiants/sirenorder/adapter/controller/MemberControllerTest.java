package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.domain.service.MemberService;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MemberControllerTest {
    static final Long MEMBER_ID = 1L;
    MemberController memberController;
    MemberService memberService;
    ControllerExceptionHandler exceptionHandler;

    @BeforeEach
    void setup() {
        memberService = mock(MemberService.class);
        memberController = new MemberController(memberService);
        exceptionHandler = new ControllerExceptionHandler();
    }

    @Test
    void testCreateCustomer() {
        when(memberService.createCustomer(anyString(), anyString())).thenReturn(MEMBER_ID);

        String email = "test@example.com";
        String password = "password";

        given()
            .standaloneSetup(memberController)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new MemberController.CreateMemberRequest(email, password))
        .when()
            .post("/api/v1/customers")
        .then()
            .status(HttpStatus.OK)
            .body("id", equalTo(MEMBER_ID.intValue()));

        verify(memberService).createCustomer(email, password);
    }

    @Test
    void testCreateCustomerInvalidEmailAddress() {
        when(memberService.createCustomer(anyString(), anyString())).thenThrow(
                new IllegalArgumentException("invalid email"));

        String email = "test@example";
        String password = "password";

        given()
            .standaloneSetup(memberController, exceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new MemberController.CreateMemberRequest(email, password))
        .when()
            .post("/api/v1/customers")
        .then()
            .status(HttpStatus.BAD_REQUEST)
            .body("msg", notNullValue());

        verify(memberService).createCustomer(email, password);
    }

    @Test
    void testCreateCustomerDuplicateEmailAddress() {
        when(memberService.createCustomer(anyString(), anyString())).then(invocationOnMock -> {
            String argument = invocationOnMock.getArgument(0, String.class);
            throw new MemberService.DuplicateEmailAddressException(new EmailAddress(argument));
        });

        String email = "test@example.com";
        String password = "password";

        given()
            .standaloneSetup(memberController, exceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new MemberController.CreateMemberRequest(email, password))
        .when()
            .post("/api/v1/customers")
        .then()
            .status(HttpStatus.CONFLICT)
            .body("msg", notNullValue());

        verify(memberService).createCustomer(email, password);
    }

    @Test
    void testCreateOwner() {
        when(memberService.createOwner(anyString(), anyString())).thenReturn(MEMBER_ID);

        String email = "test@example.com";
        String password = "password";

        given()
            .standaloneSetup(memberController)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new MemberController.CreateMemberRequest(email, password))
        .when()
            .post("/api/v1/owners")
        .then()
            .status(HttpStatus.OK)
            .body("id", equalTo(MEMBER_ID.intValue()));

        verify(memberService).createOwner(email, password);
    }

    @Test
    void testCreateOwnerInvalidEmailAddress() {
        when(memberService.createOwner(anyString(), anyString())).thenThrow(
                new IllegalArgumentException("invalid email"));

        String email = "test@example";
        String password = "password";

        given()
            .standaloneSetup(memberController, exceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new MemberController.CreateMemberRequest(email, password))
        .when()
            .post("/api/v1/owners")
        .then()
            .status(HttpStatus.BAD_REQUEST)
            .body("msg", notNullValue());

        verify(memberService).createOwner(email, password);
    }

    @Test
    void testCreateOwnerDuplicateEmailAddress() {
        when(memberService.createOwner(anyString(), anyString())).then(invocationOnMock -> {
            String argument = invocationOnMock.getArgument(0, String.class);
            throw new MemberService.DuplicateEmailAddressException(new EmailAddress(argument));
        });

        String email = "test@example.com";
        String password = "password";

        given()
            .standaloneSetup(memberController, exceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new MemberController.CreateMemberRequest(email, password))
        .when()
            .post("/api/v1/owners")
        .then()
            .status(HttpStatus.CONFLICT)
            .body("msg", notNullValue());

        verify(memberService).createOwner(email, password);
    }

    @Test
    void testCreateOwnerAtMostOneOwnerPerCafe() {
        when(memberService.createOwner(anyString(), anyString()))
                .thenThrow(new MemberService.OwnerAlreadyExistsException());

        String email = "test@example.com";
        String password = "password";

        given()
            .standaloneSetup(memberController, exceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new MemberController.CreateMemberRequest(email, password))
        .when()
            .post("/api/v1/owners")
        .then()
            .status(HttpStatus.CONFLICT)
            .body("msg", notNullValue());

        verify(memberService).createOwner(email, password);
    }
}