package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.repository.MemberRepository;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createCustomer(String email, String password) throws IllegalArgumentException {
        // TODO: make email unique
        String encoded = passwordEncoder.encode(password);
        Member customer = Member.createCustomer(new EmailAddress(email), encoded);
        Member saved = memberRepository.save(customer);
        return saved.getId();
    }

    @Transactional
    public Long createOwner(String email, String password) throws IllegalArgumentException {
        // TODO: make email unique
        String encoded = passwordEncoder.encode(password);
        Member customer = Member.createOwner(new EmailAddress(email), encoded);
        Member saved = memberRepository.save(customer);
        return saved.getId();
    }

    public Optional<Member> findMemberByEmailAddress(String email) {
        return memberRepository.findByEmailAddress(new EmailAddress(email));
    }
}
