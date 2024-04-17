package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.domain.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MemberControllerTest {
    static final Long MEMBER_ID = 1L;
    MemberController memberController;
    MemberService memberService;

    @BeforeEach
    void setup() {
        memberService = mock(MemberService.class);
        memberController = new MemberController(memberService);
    }

    @Test
    void testCreateCustomer() {
        when(memberService.createCustomer(anyString(), anyString())).thenReturn(MEMBER_ID);

        String email = "test@example.com";
        String password = "password";

        given()
            .standaloneSetup(memberController)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new MemberController.CreateCustomerRequest(email, password))
        .when()
            .post("/api/v1/customers")
        .then()
            .statusCode(200)
            .body("id", equalTo(MEMBER_ID.intValue()));

        verify(memberService).createCustomer(email, password);
    }

}