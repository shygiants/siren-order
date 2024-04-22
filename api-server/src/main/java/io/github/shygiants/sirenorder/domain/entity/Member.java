package io.github.shygiants.sirenorder.domain.entity;

import io.github.shygiants.sirenorder.domain.enumerate.Role;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import io.github.shygiants.sirenorder.utils.EmailAddressConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
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
    @Column(unique = true)
    @Convert(converter = EmailAddressConverter.class)
    private EmailAddress emailAddress;
    private String password;

    @ElementCollection
    @CollectionTable(name = "MEMBER_ROLES")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CAFE_ID")
    private Cafe cafe;

    private Member(EmailAddress emailAddress, String password, Set<Role> roles) {
        this(emailAddress, password, roles, null);
    }

    private Member(EmailAddress emailAddress, String password, Set<Role> roles, Cafe cafe) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.roles = roles;
        this.cafe = cafe;

        validateRoleAndCafeOwnership();
    }

    private void validateRoleAndCafeOwnership() {
        if (roles.contains(Role.OWNER)) {
            assert cafe != null;
        } else {
            assert cafe == null;
        }
    }

    public boolean hasAuthorityTo(Cafe cafe) {
        return this.cafe.equals(cafe) && roles.contains(Role.OWNER);
    }

    public static Member createCustomer(EmailAddress emailAddress, String password) {
        return new Member(emailAddress, password, Set.of(Role.CUSTOMER));
    }

    public static Member createOwner(EmailAddress emailAddress, String password, Cafe cafe) {
        return new Member(emailAddress, password, Set.of(Role.OWNER), cafe);
    }

    public UserDetails createUserDetails() {
        return new MemberDetails(this);
    }

    public static class MemberDetails implements UserDetails {
        private final Member member;

        private MemberDetails(Member member) {
            this.member = member;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return member.roles.stream().map(Role::toAuthority).toList();
        }

        @Override
        public String getPassword() {
            return member.password;
        }

        @Override
        public String getUsername() {
            return member.emailAddress.toString();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
