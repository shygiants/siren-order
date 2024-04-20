package io.github.shygiants.sirenorder.domain.repository;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.enumerate.Role;
import io.github.shygiants.sirenorder.domain.service.CafeService;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CafeService cafeService;
    @Autowired
    EntityManager entityManager;

    @Test
    void testSaveAndFindById() {
        Member customer = Member.createCustomer(
                new EmailAddress("test@example.com"),
                "password");

        Member saved = memberRepository.save(customer);
        entityManager.flush();

        assertThat(saved).isEqualTo(customer);

        entityManager.clear();
        entityManager.close();

        Optional<Member> memberOptional = memberRepository.findById(customer.getId());
        assertThat(memberOptional).hasValue(customer);
    }

    @Test
    void testSaveAndFindByEmailAddress() {
        Member customer = Member.createCustomer(
                new EmailAddress("test@example.com"),
                "password");

        Member saved = memberRepository.save(customer);
        entityManager.flush();

        assertThat(saved).isEqualTo(customer);

        entityManager.clear();
        entityManager.close();

        Optional<Member> memberOptional = memberRepository.findByEmailAddress(customer.getEmailAddress());
        assertThat(memberOptional).hasValue(customer);
    }

    @Test
    void testSaveAndFindByRolesAndCafe() {
        Cafe cafe = cafeService.getCafe();
        entityManager.flush();

        Member owner = Member.createOwner(
                new EmailAddress("test@example.com"),
                "password",
                cafe);

        Member saved = memberRepository.save(owner);
        entityManager.flush();

        assertThat(saved).isEqualTo(owner);

        entityManager.clear();
        entityManager.close();

        Optional<Member> memberOptional = memberRepository.findByRolesAndCafe(Role.OWNER, cafe);
        assertThat(memberOptional).hasValue(owner);
    }
}
