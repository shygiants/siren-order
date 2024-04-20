package io.github.shygiants.sirenorder.adapter.controller;

import io.github.shygiants.sirenorder.adapter.controller.exceptions.NotAuthenticatedException;
import io.github.shygiants.sirenorder.domain.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final TokenService tokenService;

    @PostMapping("/api/v1/login")
    public LoginResponse issueAccessToken(@RequestBody LoginRequest request) {
        try {
            String accessToken = tokenService.issueAccessToken(request.emailAddress, request.password);
            return new LoginResponse(accessToken);
        } catch (AuthenticationException e) {
            throw new NotAuthenticatedException();
        }
    }

    public record LoginResponse(String accessToken) {
    }

    public record LoginRequest(String emailAddress, String password) {
    }
}
