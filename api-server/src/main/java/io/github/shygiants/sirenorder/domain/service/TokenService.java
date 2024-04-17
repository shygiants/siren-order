package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.auth.Authenticator;
import io.github.shygiants.sirenorder.domain.auth.JwtGenerator;
import io.github.shygiants.sirenorder.domain.enumerate.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final Authenticator authenticator;
    private final JwtGenerator jwtGenerator;

    public String issueAccessToken(String emailAddress, String password) throws AuthenticationException {
        Collection<Role> roles = authenticator.authenticate(emailAddress, password);
        return jwtGenerator.generateToken(emailAddress, roles);
    }
}
