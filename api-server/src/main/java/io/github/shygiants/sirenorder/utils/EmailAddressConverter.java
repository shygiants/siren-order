package io.github.shygiants.sirenorder.utils;

import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import jakarta.persistence.AttributeConverter;


public class EmailAddressConverter implements AttributeConverter<EmailAddress, String> {

    @Override
    public String convertToDatabaseColumn(EmailAddress emailAddress) {
        return emailAddress == null ? null : emailAddress.toString();
    }

    @Override
    public EmailAddress convertToEntityAttribute(String s) {
        try {
            return s == null ? null : new EmailAddress(s);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to convert String to EmailAddress: " + s);
        }
    }
}
