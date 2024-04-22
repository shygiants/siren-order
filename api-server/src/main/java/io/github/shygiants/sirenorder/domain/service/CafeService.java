package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CafeService {
    public static final Long CAFE_ID = 1L;

    private final CafeRepository cafeRepository;

    public Cafe getCafe() {
        Optional<Cafe> cafeOptional = cafeRepository.findById(CAFE_ID);
        return cafeOptional.orElseGet(() -> cafeRepository.save(Cafe.fromId(CAFE_ID)));
    }

    private void checkAuthority(Member requester, Cafe cafe) {
        if (!requester.hasAuthorityTo(cafe)) {
            throw new NoAuthorityException(requester);
        }
    }

    public void openCafe(Member requester) {
        Cafe cafe = getCafe();
        checkAuthority(requester, cafe);

        cafe.open();
        cafeRepository.save(cafe);
    }

    public void closeCafe(Member requester) {
        Cafe cafe = getCafe();
        checkAuthority(requester, cafe);

        cafe.close();
        cafeRepository.save(cafe);
    }

    public static class NoAuthorityException extends IllegalCallerException {
        public NoAuthorityException(Member member) {
            super("Requester " + member.getEmailAddress() + " has no authority");
        }
    }
}
