package io.github.shygiants.sirenorder.adapter.security;

import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberService.findMemberByEmailAddress(username);
        return member.orElseThrow().createUserDetails();
    }
}
