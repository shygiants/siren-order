package io.github.shygiants.sirenorder.domain.valueobject;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EmailAddressTest {

    @Test
    void testValidEmailAddress() {
        // GIVEN
        String validEmailAddress = "test@example.com";

        // WHEN
        EmailAddress emailAddress = new EmailAddress(validEmailAddress);

        // THEN
        assertThat(emailAddress.toString()).isEqualTo(validEmailAddress);
    }

    @Test
    void testInvalidEmailAddress() {
        // GIVEN
        String invalidEmailAddress = "test@example";

        // WHEN
        Throwable throwable = catchThrowable(() -> new EmailAddress(invalidEmailAddress));

        // THEN
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(invalidEmailAddress);
    }

}