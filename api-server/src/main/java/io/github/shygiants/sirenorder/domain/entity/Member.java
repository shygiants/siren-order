package io.github.shygiants.sirenorder.domain.entity;

import io.github.shygiants.sirenorder.domain.enumerate.Role;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import io.github.shygiants.sirenorder.utils.EmailAddressConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Member {
    @Getter
    @GeneratedValue
    @Id
    private Long id;
    @Convert(converter = EmailAddressConverter.class)
    private EmailAddress emailAddress;
    private String password;

    @ElementCollection
    @CollectionTable(name = "MEMBER_ROLES")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private Member(EmailAddress emailAddress, String password, Set<Role> roles) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.roles = roles;
    }

    public static Member createCustomer(EmailAddress emailAddress, String password) {
        return new Member(emailAddress, password, Set.of(Role.CUSTOMER));
    }
}
