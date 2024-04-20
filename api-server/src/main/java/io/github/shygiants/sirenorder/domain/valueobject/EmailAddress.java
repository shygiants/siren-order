package io.github.shygiants.sirenorder.domain.valueobject;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class EmailAddress {
    private static final String EMAIL_ADDRESS_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private final String emailAddress;

    public EmailAddress(String emailAddress) throws IllegalArgumentException {
        this.emailAddress = emailAddress;
        validate();
    }

    private void validate() {
        if (!emailAddress.matches(EMAIL_ADDRESS_PATTERN)) {
            throw new IllegalArgumentException("Invalid email address format: " + emailAddress);
        }
    }

    @Override
    public String toString() {
        return emailAddress;
    }
}
