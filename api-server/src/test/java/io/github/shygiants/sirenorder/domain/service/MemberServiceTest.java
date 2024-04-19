package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.enumerate.Role;
import io.github.shygiants.sirenorder.domain.repository.MemberRepository;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceTest {
    static final Long MEMBER_ID = 1L;

    MemberService memberService;
    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;
    CafeService cafeService;
    Cafe cafe;

    @BeforeEach
    void setup() {
        memberRepository = mock(MemberRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        cafeService = mock(CafeService.class);
        cafe = mock(Cafe.class);
        memberService = new MemberService(memberRepository, passwordEncoder, cafeService);
    }

    @Test
    void testCreateCustomer() {
        // GIVEN
        when(memberRepository.findByEmailAddress(any())).thenReturn(Optional.empty());
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
        verify(memberRepository).findByEmailAddress(new EmailAddress(emailAddress));
        verify(passwordEncoder).encode(password);
        verify(memberRepository).save(argThat(mem -> mem.equals(
                Member.createCustomer(
                        new EmailAddress(emailAddress),
                        passwordEncoder.encode(password)))));
        assertThat(createdCustomerId).isEqualTo(MEMBER_ID);
    }

    @Test
    void testCreateCustomerUniqueEmailAddress() {
        // GIVEN
        when(memberRepository.findByEmailAddress(any())).thenReturn(Optional.of(mock(Member.class)));
        when(memberRepository.save(any(Member.class))).thenThrow(new DataIntegrityViolationException(""));
        when(passwordEncoder.encode(any(String.class))).then(invocationOnMock -> invocationOnMock.getArgument(0));
        String email = "test@example.com";

        // WHEN
        Throwable throwable = catchThrowable(() -> memberService.createCustomer(email, "password"));

        // THEN
        verify(memberRepository, never()).save(any());
        assertThat(throwable)
                .isInstanceOf(MemberService.DuplicateEmailAddressException.class)
                .hasMessageContaining(email);
    }

    @Test
    void testCreateCustomerUniqueEmailAddressRaceCondition() {
        // GIVEN
        when(memberRepository.findByEmailAddress(any())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenThrow(new DataIntegrityViolationException("EMAIL_ADDRESS"));
        when(passwordEncoder.encode(any(String.class))).then(invocationOnMock -> invocationOnMock.getArgument(0));
        String email = "test@example.com";

        // WHEN
        Throwable throwable = catchThrowable(() -> memberService.createCustomer(email, "password"));

        // THEN
        verify(memberRepository).save(any());
        assertThat(throwable)
                .isInstanceOf(MemberService.DuplicateEmailAddressException.class)
                .hasMessageContaining(email);
    }

    @Test
    void testCreateOwner() {
        // GIVEN
        when(memberRepository.findByEmailAddress(any())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).then(invocationOnMock -> {
            Member member = spy(invocationOnMock.getArgument(0, Member.class));
            when(member.getId()).thenReturn(MEMBER_ID);
            return member;
        });
        when(memberRepository.findByRolesAndCafe(Role.OWNER, cafe)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).then(invocationOnMock -> invocationOnMock.getArgument(0));
        when(cafeService.getCafe()).thenReturn(cafe);
        String emailAddress = "test@example.com";
        String password = "password";

        // WHEN
        Long createdCustomerId = memberService.createOwner(emailAddress, password);

        // THEN
        verify(cafeService).getCafe();
        verify(memberRepository).findByRolesAndCafe(Role.OWNER, cafe);
        verify(memberRepository).findByEmailAddress(new EmailAddress(emailAddress));
        verify(passwordEncoder).encode(password);
        verify(memberRepository).save(argThat(mem -> mem.equals(
                Member.createOwner(
                        new EmailAddress(emailAddress),
                        passwordEncoder.encode(password),
                        cafe))));
        assertThat(createdCustomerId).isEqualTo(MEMBER_ID);
    }

    @Test
    void testCreateOwnerUniqueEmailAddress() {
        // GIVEN
        when(cafeService.getCafe()).thenReturn(cafe);
        when(memberRepository.findByRolesAndCafe(Role.OWNER, cafe)).thenReturn(Optional.empty());
        when(memberRepository.findByEmailAddress(any())).thenReturn(Optional.of(mock(Member.class)));
        when(memberRepository.save(any(Member.class))).thenThrow(new DataIntegrityViolationException("EMAIL_ADDRESS"));
        when(passwordEncoder.encode(any(String.class))).then(invocationOnMock -> invocationOnMock.getArgument(0));
        String email = "test@example.com";

        // WHEN
        Throwable throwable = catchThrowable(() -> memberService.createOwner(email, "password"));

        // THEN
        verify(memberRepository, never()).save(any());
        assertThat(throwable)
                .isInstanceOf(MemberService.DuplicateEmailAddressException.class)
                .hasMessageContaining(email);
    }

    @Test
    void testCreateOwnerUniqueEmailAddressRaceCondition() {
        // GIVEN
        when(cafeService.getCafe()).thenReturn(cafe);
        when(memberRepository.findByRolesAndCafe(Role.OWNER, cafe)).thenReturn(Optional.empty());
        when(memberRepository.findByEmailAddress(any())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenThrow(new DataIntegrityViolationException("EMAIL_ADDRESS"));
        when(passwordEncoder.encode(any(String.class))).then(invocationOnMock -> invocationOnMock.getArgument(0));
        String email = "test@example.com";

        // WHEN
        Throwable throwable = catchThrowable(() -> memberService.createOwner(email, "password"));

        // THEN
        verify(memberRepository).save(any(Member.class));
        assertThat(throwable)
                .isInstanceOf(MemberService.DuplicateEmailAddressException.class)
                .hasMessageContaining(email);
    }

    @Test
    void testCreateOwnerAllowsAtMostOne() {
        // GIVEN
        when(memberRepository.findByEmailAddress(any())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).then(invocationOnMock -> {
            Member member = spy(invocationOnMock.getArgument(0, Member.class));
            when(member.getId()).thenReturn(MEMBER_ID);
            return member;
        });
        when(memberRepository.findByRolesAndCafe(Role.OWNER, cafe)).thenReturn(Optional.of(mock(Member.class)));
        when(passwordEncoder.encode(any(String.class))).then(invocationOnMock -> invocationOnMock.getArgument(0));
        when(cafeService.getCafe()).thenReturn(cafe);
        String emailAddress = "test@example.com";
        String password = "password";

        // WHEN
        Throwable throwable = catchThrowable(() -> memberService.createOwner(emailAddress, password));

        // THEN
        verify(cafeService).getCafe();
        verify(memberRepository).findByRolesAndCafe(Role.OWNER, cafe);
        verify(memberRepository, never()).save(any());
        assertThat(throwable).isInstanceOf(MemberService.OwnerAlreadyExistsException.class);
    }

    @Test
    void testCreateOwnerAllowsAtMostOneRaceCondition() {
        // GIVEN
        when(memberRepository.findByEmailAddress(any())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenThrow(new DataIntegrityViolationException("CAFE_ID"));
        when(memberRepository.findByRolesAndCafe(Role.OWNER, cafe)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).then(invocationOnMock -> invocationOnMock.getArgument(0));
        when(cafeService.getCafe()).thenReturn(cafe);
        String emailAddress = "test@example.com";
        String password = "password";

        // WHEN
        Throwable throwable = catchThrowable(() -> memberService.createOwner(emailAddress, password));

        // THEN
        verify(cafeService).getCafe();
        verify(memberRepository).findByRolesAndCafe(Role.OWNER, cafe);
        verify(memberRepository).findByEmailAddress(new EmailAddress(emailAddress));
        verify(passwordEncoder).encode(password);
        verify(memberRepository).save(argThat(mem -> mem.equals(
                Member.createOwner(
                        new EmailAddress(emailAddress),
                        passwordEncoder.encode(password),
                        cafe))));
        assertThat(throwable).isInstanceOf(MemberService.OwnerAlreadyExistsException.class);
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