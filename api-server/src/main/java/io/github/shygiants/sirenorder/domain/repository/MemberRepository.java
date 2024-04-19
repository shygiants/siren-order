package io.github.shygiants.sirenorder.domain.repository;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.enumerate.Role;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAddress(EmailAddress emailAddress);
    Optional<Member> findByRolesAndCafe(Role role, Cafe cafe);
}
