package io.github.shygiants.sirenorder.domain.valueobject;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailAddressTest {

    @Test
    void testValidEmailAddress() {
        // GIVEN
        String validEmailAddress = "test@example.com";

        // WHEN
        Throwable throwable = Assertions.catchThrowable(() -> new EmailAddress(validEmailAddress));

        // THEN
        Assertions.assertThat(throwable).isNull();
    }

    @Test
    void testInvalidEmailAddress() {
        // GIVEN
        String invalidEmailAddress = "test@example";

        // WHEN
        Throwable throwable = Assertions.catchThrowable(() -> new EmailAddress(invalidEmailAddress));

        // THEN
        Assertions.assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(invalidEmailAddress);
    }

}