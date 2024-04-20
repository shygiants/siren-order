package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.domain.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoginControllerTest {
    LoginController loginController;
    TokenService tokenService;
    ControllerExceptionHandler exceptionHandler;


    @BeforeEach
    void setup() {
        tokenService = mock(TokenService.class);
        loginController = new LoginController(tokenService);
        exceptionHandler = new ControllerExceptionHandler();
    }

    @Test
    void testIssueAccessToken() {
        String token = "token";
        when(tokenService.issueAccessToken(anyString(), anyString())).thenReturn(token);
        String email = "test@example.com";
        String password = "password";

        given()
            .standaloneSetup(loginController, exceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new LoginController.LoginRequest(email, password))
        .when()
            .post("/api/v1/login")
        .then()
            .status(HttpStatus.OK)
            .body("accessToken", equalTo(token));

        verify(tokenService).issueAccessToken(email, password);
    }

    @Test
    void testIssueAccessTokenFail() {
        String token = "token";
        when(tokenService.issueAccessToken(anyString(), anyString())).thenThrow(new BadCredentialsException(""));
        String email = "test@example.com";
        String password = "password";

        given()
            .standaloneSetup(loginController, exceptionHandler)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new LoginController.LoginRequest(email, password))
        .when()
            .post("/api/v1/login")
        .then()
            .status(HttpStatus.UNAUTHORIZED)
            .body("msg", notNullValue());

        verify(tokenService).issueAccessToken(email, password);
    }
}