package io.github.shygiants.sirenorder.domain.auth;

import io.github.shygiants.sirenorder.domain.enumerate.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class Authenticator {
    private final AuthenticationManager authenticationManager;

    public Collection<Role> authenticate(String emailAddress, String password) throws AuthenticationException {
        Authentication authReq = createAuthRequest(emailAddress, password);
        Authentication authResp = authenticationManager.authenticate(authReq);

        return convertAuthoritiesToRoles(authResp.getAuthorities());
    }

    private Authentication createAuthRequest(String emailAddress, String password) {
        return UsernamePasswordAuthenticationToken.unauthenticated(emailAddress, password);
    }

    private Collection<Role> convertAuthoritiesToRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(Role::fromAuthority).toList();
    }
}
