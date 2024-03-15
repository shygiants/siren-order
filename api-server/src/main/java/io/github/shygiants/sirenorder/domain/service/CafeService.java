package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CafeService {
    public static final Long CAFE_ID = 1L;

    private final CafeRepository cafeRepository;

    @Transactional
    public Cafe getCafe() {
        Optional<Cafe> cafeOptional = cafeRepository.findById(CAFE_ID);
        return cafeOptional.orElseGet(() -> cafeRepository.save(Cafe.fromId(CAFE_ID)));
    }
}
