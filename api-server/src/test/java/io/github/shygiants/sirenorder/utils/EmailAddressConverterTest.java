package io.github.shygiants.sirenorder.utils;

import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


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
        assertThat(reconstructed).isEqualTo(emailAddress);
    }

    @Test
    void testConvertNull() {
        // GIVEN
        EmailAddress emailAddress = null;


        // WHEN
        String converted = converter.convertToDatabaseColumn(emailAddress);
        EmailAddress reconstructed = converter.convertToEntityAttribute(converted);

        // THEN
        assertThat(converted).isNull();
        assertThat(reconstructed).isNull();
        assertThat(reconstructed).isEqualTo(emailAddress);
    }
}