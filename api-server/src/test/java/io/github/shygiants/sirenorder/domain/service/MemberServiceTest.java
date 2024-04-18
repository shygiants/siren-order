package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.repository.MemberRepository;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceTest {
    static final Long MEMBER_ID = 1L;

    MemberService memberService;
    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        memberRepository = mock(MemberRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        memberService = new MemberService(memberRepository, passwordEncoder);
    }

    @Test
    void testCreateCustomer() {
        // GIVEN
        when(memberRepository.save(any(Member.class))).then(invocationOnMock -> {
            Member member = spy(invocationOnMock.getArgument(0, Member.class));
            when(member.getId()).thenReturn(MEMBER_ID);
            return member;
        });
        when(passwordEncoder.encode(any(String.class))).then(invocationOnMock -> invocationOnMock.getArgument(0));
        String emailAddress = "test@example.com";
        String password = "password";

        // WHEN
        Long createdCustomerId = memberService.createCustomer(emailAddress, password);

        // THEN
        verify(passwordEncoder).encode(password);
        verify(memberRepository).save(argThat(mem -> mem.equals(
                Member.createCustomer(
                        new EmailAddress(emailAddress),
                        passwordEncoder.encode(password)))));
        assertThat(createdCustomerId).isEqualTo(MEMBER_ID);
    }

    @Test
    void testCreateOwner() {
        // GIVEN
        when(memberRepository.save(any(Member.class))).then(invocationOnMock -> {
            Member member = spy(invocationOnMock.getArgument(0, Member.class));
            when(member.getId()).thenReturn(MEMBER_ID);
            return member;
        });
        when(passwordEncoder.encode(any(String.class))).then(invocationOnMock -> invocationOnMock.getArgument(0));
        String emailAddress = "test@example.com";
        String password = "password";

        // WHEN
        Long createdCustomerId = memberService.createOwner(emailAddress, password);

        // THEN
        verify(passwordEncoder).encode(password);
        verify(memberRepository).save(argThat(mem -> mem.equals(
                Member.createOwner(
                        new EmailAddress(emailAddress),
                        passwordEncoder.encode(password)))));
        assertThat(createdCustomerId).isEqualTo(MEMBER_ID);
    }


    @Test
    void testFindMemberByEmailAddress() {
        // GIVEN
        when(memberRepository.findByEmailAddress(any(EmailAddress.class))).then(invocationOnMock -> {
            EmailAddress emailAddress = invocationOnMock.getArgument(0);
            return Optional.of(Member.createCustomer(emailAddress, "password"));
        });
        String emailAddress = "test@example.com";

        // WHEN
        Optional<Member> member = memberService.findMemberByEmailAddress(emailAddress);

        // THEN
        verify(memberRepository).findByEmailAddress(new EmailAddress(emailAddress));
        assertThat(member).hasValueSatisfying(mem ->
                assertThat(mem.getEmailAddress().toString()).isEqualTo(emailAddress));
    }
}