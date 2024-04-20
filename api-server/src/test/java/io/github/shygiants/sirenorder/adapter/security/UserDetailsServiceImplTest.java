package io.github.shygiants.sirenorder.adapter.security;


import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.service.MemberService;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {
    MemberService memberService;
    UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setup() {
        memberService = mock(MemberService.class);
        userDetailsService = new UserDetailsServiceImpl(memberService);
    }

    @Test
    void testLoadUserByUsername() {
        // GIVEN
        when(memberService.findMemberByEmailAddress(anyString())).then(invocationOnMock -> {
            String emailAddress = invocationOnMock.getArgument(0, String.class);
            return Optional.of(Member.createCustomer(new EmailAddress(emailAddress), "password"));
        });
        String emailAddress = "test@example.com";

        // WHEN
        UserDetails userDetails = userDetailsService.loadUserByUsername(emailAddress);

        // THEN
        verify(memberService).findMemberByEmailAddress(emailAddress);
        assertThat(userDetails).matches(userDetails1 ->
                userDetails1.getUsername().equals(emailAddress));
    }
}