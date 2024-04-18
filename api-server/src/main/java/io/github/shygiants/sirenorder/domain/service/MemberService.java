package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.enumerate.Role;
import io.github.shygiants.sirenorder.domain.repository.MemberRepository;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private void validateUniqueEmailAddress(EmailAddress emailAddress) {
        Optional<Member> memberOptional = memberRepository.findByEmailAddress(emailAddress);
        if (memberOptional.isPresent()) {
            throw new DuplicateEmailAddressException(emailAddress);
        }
    }

    private void validateAtMostOneOwner() {
        Optional<Member> memberOptional = memberRepository.findByRoles(Role.OWNER);
        if (memberOptional.isPresent()) {
            throw new OwnerAlreadyExistsException();
        }
    }

    private Long createMember(String email, String password, MemberFactory memberFactory) {
        EmailAddress emailAddress = new EmailAddress(email);
        validateUniqueEmailAddress(emailAddress);

        String encoded = passwordEncoder.encode(password);
        Member customer = memberFactory.createMember(emailAddress, encoded);
        try {
            Member saved = memberRepository.save(customer);
            return saved.getId();
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailAddressException(emailAddress);
        }
    }

    public Long createCustomer(String email, String password) throws IllegalArgumentException {
        return createMember(email, password, Member::createCustomer);
    }

    public Long createOwner(String email, String password) throws IllegalArgumentException {
        // TODO: Consider race condition
        validateAtMostOneOwner();
        return createMember(email, password, Member::createOwner);
    }

    public Optional<Member> findMemberByEmailAddress(String email) {
        return memberRepository.findByEmailAddress(new EmailAddress(email));
    }

    public static class DuplicateEmailAddressException extends IllegalArgumentException {
        public DuplicateEmailAddressException(EmailAddress emailAddress) {
            super("Duplicate email: " + emailAddress);
        }
    }

    public static class OwnerAlreadyExistsException extends IllegalStateException {
        public OwnerAlreadyExistsException() {
            super("Owner already exists");
        }
    }

    private interface MemberFactory {
        Member createMember(EmailAddress emailAddress, String password);
    }
}
