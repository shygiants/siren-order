package io.github.shygiants.sirenorder.domain.auth;


import io.github.shygiants.sirenorder.domain.enumerate.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticatorTest {
    AuthenticationManager authenticationManager;
    Authenticator authenticator;

    @BeforeEach
    void setup() {
        authenticationManager = mock(AuthenticationManager.class);
        authenticator = new Authenticator(authenticationManager);
    }

    @Test
    void testAuthenticateSuccess() {
        // GIVEN
        Collection<? extends GrantedAuthority> authorities = List.of(Role.CUSTOMER.toAuthority());
        when(authenticationManager.authenticate(any())).then(invocationOnMock -> {
            Authentication authentication = mock(Authentication.class);
            when(authentication.getAuthorities()).then(invocationOnMock1 -> authorities);
            when(authentication.isAuthenticated()).thenReturn(true);
            return authentication;
        });
        String emailAddress = "test@example.com";
        String password = "password";

        // WHEN
        Collection<Role> roles = authenticator.authenticate(emailAddress, password);

        // THEN
        verify(authenticationManager).authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(emailAddress, password));
        assertThat(roles).containsExactly(Role.CUSTOMER);
    }

    @Test
    void testAuthenticateFail() {
        // GIVEN
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));
        String emailAddress = "test@example.com";
        String password = "password";

        // WHEN
        Throwable throwable = catchThrowable(() -> authenticator.authenticate(emailAddress, password));

        // THEN
        verify(authenticationManager).authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(emailAddress, password));
        assertThat(throwable).isInstanceOf(AuthenticationException.class);
    }

}