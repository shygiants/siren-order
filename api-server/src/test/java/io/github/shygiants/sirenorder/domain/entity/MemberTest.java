package io.github.shygiants.sirenorder.domain.entity;

import io.github.shygiants.sirenorder.domain.enumerate.Role;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;


class MemberTest {

    @Test
    void testCreateCustomer() {
        // GIVEN
        EmailAddress emailAddress = new EmailAddress("test@example.com");
        String password = "password";

        // WHEN
        Member customer = Member.createCustomer(emailAddress, password);

        // THEN
        assertThat(customer.getEmailAddress()).isEqualTo(emailAddress);
        assertThat(customer.getPassword()).isEqualTo(password);
        assertThat(customer.getRoles()).containsExactly(Role.CUSTOMER);
        assertThat(customer.getCafe()).isNull();
    }

    @Test
    void testCreateOwner() {
        // GIVEN
        EmailAddress emailAddress = new EmailAddress("test@example.com");
        String password = "password";
        Cafe cafe = new Cafe();

        // WHEN
        Member customer = Member.createOwner(emailAddress, password, cafe);

        // THEN
        assertThat(customer.getEmailAddress()).isEqualTo(emailAddress);
        assertThat(customer.getPassword()).isEqualTo(password);
        assertThat(customer.getCafe()).isEqualTo(cafe);
        assertThat(customer.getRoles()).containsExactly(Role.OWNER);
    }

    @Test
    void testCreateUserDetails() {
        // GIVEN
        String emailAddress = "test@example.com";
        String password = "password";
        Member customer = Member.createCustomer(new EmailAddress(emailAddress), password);

        // WHEN
        UserDetails userDetails = customer.createUserDetails();

        // THEN
        assertThat(userDetails.getAuthorities())
                .hasSize(1)
                .allMatch(grantedAuthority -> Role.fromAuthority(grantedAuthority).equals(Role.CUSTOMER));
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getUsername()).isEqualTo(emailAddress);
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();
    }
}