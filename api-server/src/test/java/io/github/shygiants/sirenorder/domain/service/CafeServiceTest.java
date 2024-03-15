package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.repository.CafeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;


class CafeServiceTest {
    Cafe cafe;
    CafeService cafeService;
    CafeRepository cafeRepository;

    @BeforeEach
    void setUp() {
        cafe = Cafe.fromId(CafeService.CAFE_ID);
        cafeRepository = mock(CafeRepository.class);
        cafeService = new CafeService(cafeRepository);
    }

    @Test
    void testGetCafeIfCafeExists() {
        // GIVEN
        when(cafeRepository.findById(CafeService.CAFE_ID)).thenReturn(Optional.of(this.cafe));

        // WHEN
        Cafe cafe = cafeService.getCafe();

        // THEN
        Assertions.assertThat(cafe.getId()).isEqualTo(CafeService.CAFE_ID);
        verify(cafeRepository).findById(CafeService.CAFE_ID);
    }

    @Test
    void testGetCafeIfCafeNotExists() {
        // GIVEN
        when(cafeRepository.findById(CafeService.CAFE_ID)).thenReturn(Optional.empty());
        when(cafeRepository.save(any(Cafe.class))).then(invocationOnMock -> invocationOnMock.getArgument(0));

        // WHEN
        Cafe cafe = cafeService.getCafe();

        // THEN
        Assertions.assertThat(cafe.getId()).isEqualTo(CafeService.CAFE_ID);
        verify(cafeRepository).findById(CafeService.CAFE_ID);
        verify(cafeRepository).save(argThat(arg -> arg.getId().equals(CafeService.CAFE_ID)));
    }
}