package io.github.shygiants.sirenorder.adapter.security;

import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedMemberHolder {
    private final MemberService memberService;

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Member getMember() {
        Authentication authentication = getAuthentication();
        return memberService.findMemberByEmailAddress(authentication.getName()).orElseThrow();
    }
}
