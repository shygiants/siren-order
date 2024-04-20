package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.auth.Authenticator;
import io.github.shygiants.sirenorder.domain.auth.JwtGenerator;
import io.github.shygiants.sirenorder.domain.enumerate.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TokenServiceTest {
    TokenService tokenService;
    Authenticator authenticator;
    JwtGenerator jwtGenerator;


    @BeforeEach
    void setup() {
        authenticator = mock(Authenticator.class);
        jwtGenerator = mock(JwtGenerator.class);
        tokenService = new TokenService(authenticator, jwtGenerator);
    }

    @Test
    void testIssueAccessToken() {
        // GIVEN
        Collection<Role> roles = List.of(Role.CUSTOMER);
        String emailAddress = "test@example.com";
        String token = "token";
        when(authenticator.authenticate(eq(emailAddress), anyString())).thenReturn(roles);
        when(jwtGenerator.generateToken(eq(emailAddress), any())).thenReturn(token);
        String password = "password";

        // WHEN
        String tokenIssued = tokenService.issueAccessToken(emailAddress, password);

        // THEN
        verify(authenticator).authenticate(emailAddress, password);
        verify(jwtGenerator).generateToken(emailAddress, roles);
        assertThat(tokenIssued).isEqualTo(token);
    }

    @Test
    void testIssueAccessTokenFail() {
        // GIVEN
        String emailAddress = "test@example.com";
        String token = "token";
        when(authenticator.authenticate(anyString(), anyString())).thenThrow(new BadCredentialsException(""));
        when(jwtGenerator.generateToken(anyString(), any())).thenReturn(token);
        String password = "password";

        // WHEN
        Throwable throwable = catchThrowable(() -> tokenService.issueAccessToken(emailAddress, password));

        // THEN
        verify(authenticator).authenticate(emailAddress, password);
        verify(jwtGenerator, never()).generateToken(anyString(), any());
        assertThat(throwable).isInstanceOf(AuthenticationException.class);
    }
}