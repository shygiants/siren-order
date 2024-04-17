package io.github.shygiants.sirenorder.utils;

import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class EmailAddressConverterTest {
    EmailAddressConverter converter = new EmailAddressConverter();

    @Test
    void testConvert() {
        // GIVEN
        EmailAddress emailAddress = new EmailAddress("test@example.com");


        // WHEN
        String converted = converter.convertToDatabaseColumn(emailAddress);
        EmailAddress reconstructed = converter.convertToEntityAttribute(converted);

        // THEN
        Assertions.assertThat(reconstructed).isEqualTo(emailAddress);
    }

    @Test
    void testConvertNull() {
        // GIVEN
        EmailAddress emailAddress = null;


        // WHEN
        String converted = converter.convertToDatabaseColumn(emailAddress);
        EmailAddress reconstructed = converter.convertToEntityAttribute(converted);

        // THEN
        Assertions.assertThat(converted).isNull();
        Assertions.assertThat(reconstructed).isNull();
        Assertions.assertThat(reconstructed).isEqualTo(emailAddress);
    }
}