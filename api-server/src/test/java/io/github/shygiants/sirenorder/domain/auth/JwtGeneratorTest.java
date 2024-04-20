package io.github.shygiants.sirenorder.domain.auth;


import io.github.shygiants.sirenorder.domain.enumerate.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtGeneratorTest {
    TemporalAmount duration;
    JwtEncoder jwtEncoder;
    JwtGenerator jwtGenerator;

    @BeforeEach
    void setup() {
        duration = Duration.ofDays(30);
        jwtEncoder = mock(JwtEncoder.class);
        jwtGenerator = new JwtGenerator(duration, jwtEncoder);
    }

    @Test
    void testGenerate() {
        // GIVEN
        when(jwtEncoder.encode(any())).thenReturn(mock(Jwt.class));
        String emailAddress = "test@example.com";
        List<Role> roles = List.of(Role.CUSTOMER);
        Instant beforeIssue = Instant.now();

        // WHEN
        jwtGenerator.generateToken(emailAddress, roles);

        // THEN
        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder).encode(captor.capture());
        JwtEncoderParameters parameters = captor.getValue();
        assertThat(parameters.getJwsHeader().getAlgorithm()).isEqualTo(SignatureAlgorithm.RS256);
        JwtClaimsSet claims = parameters.getClaims();
        assertThat(claims.getSubject()).isEqualTo(emailAddress);
        assertThat(claims.getId()).isNotNull().isNotEmpty();
        Instant issuedAt = claims.getIssuedAt();
        assertThat(issuedAt).isBetween(beforeIssue, Instant.now());
        assertThat(claims.getExpiresAt()).isEqualTo(issuedAt.plus(duration));
        assertThat(claims.getClaimAsStringList(JwtGenerator.ROLE_CLAIM))
                .containsExactlyInAnyOrderElementsOf(roles.stream().map(Role::toString).toList());
    }
}