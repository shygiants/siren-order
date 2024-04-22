package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.adapter.controller.exceptions.NotAuthorizedException;
import io.github.shygiants.sirenorder.adapter.security.AuthenticatedMemberHolder;
import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.service.CafeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;
    private final AuthenticatedMemberHolder authenticatedMemberHolder;

    @GetMapping("/api/v1/cafe")
    public GetCafeResponse getCafe() {
        Cafe cafe = cafeService.getCafe();
        return new GetCafeResponse(cafe.getIsOpen());
    }

    @PostMapping("/api/v1/cafe/open")
    public void openCafe() {
        try {
            cafeService.openCafe(authenticatedMemberHolder.getMember());
        } catch (CafeService.NoAuthorityException e) {
            throw new NotAuthorizedException(e);
        }
    }

    @PostMapping("/api/v1/cafe/close")
    public void closeCafe() {
        try {
            cafeService.closeCafe(authenticatedMemberHolder.getMember());
        } catch (CafeService.NoAuthorityException e) {
            throw new NotAuthorizedException(e);
        }
    }

    @Data
    @AllArgsConstructor
    static class GetCafeResponse {
        private Boolean open;
    }
}
