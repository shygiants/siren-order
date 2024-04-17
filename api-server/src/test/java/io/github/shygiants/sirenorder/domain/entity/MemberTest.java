package io.github.shygiants.sirenorder.domain.entity;

import io.github.shygiants.sirenorder.domain.enumerate.Role;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void testCreateCustomer() {
        // GIVEN
        EmailAddress emailAddress = new EmailAddress("test@example.com");
        String password = "password";

        // WHEN
        Member customer = Member.createCustomer(emailAddress, password);

        // THEN
        Assertions.assertThat(customer.getEmailAddress()).isEqualTo(emailAddress);
        Assertions.assertThat(customer.getPassword()).isEqualTo(password);
        Assertions.assertThat(customer.getRoles()).containsExactly(Role.CUSTOMER);
    }
}