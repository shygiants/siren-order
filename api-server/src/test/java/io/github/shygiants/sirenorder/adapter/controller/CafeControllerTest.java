package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.service.CafeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;

class CafeControllerTest {
    CafeController cafeController;
    CafeService cafeService;

    @BeforeEach
    void setup() {
        cafeService = mock(CafeService.class);
        cafeController = new CafeController(cafeService);
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
}