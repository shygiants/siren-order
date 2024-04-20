package io.github.shygiants.sirenorder.domain.enumerate;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


class RoleTest {
    static final String validAuthorityString = "ROLE_CUSTOMER";
    static final String invalidAuthorityString = "CUSTOMER";

    @Test
    void testGetAuthorityString() {
        // GIVEN
        Role role = Role.CUSTOMER;

        // WHEN
        String authorityString = role.getAuthorityString();

        // THEN
        assertThat(authorityString).isEqualTo("ROLE_CUSTOMER");
    }

    @Test
    void testToAuthority() {
        // GIVEN
        Role role = Role.CUSTOMER;

        // WHEN
        GrantedAuthority authority = role.toAuthority();

        // THEN
        assertThat(authority.getAuthority()).isEqualTo("ROLE_CUSTOMER");
    }

    @Test
    void testFromValidAuthorityString() {
        // GIVEN

        // WHEN
        Role role = Role.fromAuthorityString(validAuthorityString);

        // THEN
        assertThat(role).isEqualTo(Role.CUSTOMER);
    }

    @Test
    void testFromInvalidAuthorityString() {
        // GIVEN

        // WHEN
        Throwable throwable = catchThrowable(() -> Role.fromAuthorityString(invalidAuthorityString));

        // THEN
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(invalidAuthorityString);
    }

    @Test
    void testFromValidAuthority() {
        // GIVEN
        GrantedAuthority authority = new SimpleGrantedAuthority(validAuthorityString);

        // WHEN
        Role role = Role.fromAuthority(authority);

        // THEN
        assertThat(role).isEqualTo(Role.CUSTOMER);
    }

    @Test
    void testFromInvalidAuthority() {
        // GIVEN
        GrantedAuthority authority = new SimpleGrantedAuthority(invalidAuthorityString);

        // WHEN
        Throwable throwable = catchThrowable(() -> Role.fromAuthority(authority));

        // THEN
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(invalidAuthorityString);
    }
}