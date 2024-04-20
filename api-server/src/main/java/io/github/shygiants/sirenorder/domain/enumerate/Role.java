package io.github.shygiants.sirenorder.domain.enumerate;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    CUSTOMER,
    OWNER;

    private static final String AUTHORITY_PREFIX = "ROLE_";

    public static Role fromAuthorityString(String authorityString) throws IllegalArgumentException {
        validateAuthorityString(authorityString);
        return Role.valueOf(authorityString.substring(AUTHORITY_PREFIX.length()));
    }

    private static void validateAuthorityString(String authorityString) {
        if (!authorityString.startsWith(AUTHORITY_PREFIX)) {
            throw new IllegalArgumentException("Invalid authorityString: " + authorityString);
        }
    }

    public static Role fromAuthority(GrantedAuthority authority) throws IllegalArgumentException {
        return fromAuthorityString(authority.getAuthority());
    }

    public String getAuthorityString() {
        return AUTHORITY_PREFIX + this;
    }

    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(getAuthorityString());
    }
}
