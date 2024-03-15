package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.service.CafeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CafeController {
    private final CafeService cafeService;

    @GetMapping("/api/v1/cafe")
    public GetCafeResponse getCafe() {
        Cafe cafe = cafeService.getCafe();
        return new GetCafeResponse(cafe.getOpen());
    }

    @Data
    @AllArgsConstructor
    static class GetCafeResponse {
        private Boolean open;
    }
}
