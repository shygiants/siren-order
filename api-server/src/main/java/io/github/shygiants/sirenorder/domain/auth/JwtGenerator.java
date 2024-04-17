package io.github.shygiants.sirenorder.domain.auth;

import io.github.shygiants.sirenorder.domain.enumerate.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtGenerator {
    public static final String ROLE_CLAIM = "role";

    private final TemporalAmount duration;
    private final JwtEncoder jwtEncoder;

    public String generateToken(String emailAddress, Collection<Role> roles) {
        JwsHeader.Builder jwsHeaderBuilder = JwsHeader.with(SignatureAlgorithm.RS256);
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(duration);

        claimsBuilder
                .subject(emailAddress)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .id(UUID.randomUUID().toString());

        List<String> roleStrs = roles.stream().map(Role::toString).toList();
        claimsBuilder.claim(ROLE_CLAIM, roleStrs);

        JwsHeader jwsHeader = jwsHeaderBuilder.build();
        JwtClaimsSet claims = claimsBuilder.build();
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
        return jwt.getTokenValue();
    }
}
