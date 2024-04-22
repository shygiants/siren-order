package io.github.shygiants.sirenorder.domain.service;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.repository.CafeRepository;
import io.github.shygiants.sirenorder.domain.valueobject.EmailAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;


class CafeServiceTest {
    Cafe cafe;
    Member requester;
    CafeService cafeService;
    CafeRepository cafeRepository;

    @BeforeEach
    void setUp() {
        cafe = Cafe.fromId(CafeService.CAFE_ID);
        requester = mock(Member.class);
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
        assertThat(cafe.getId()).isEqualTo(CafeService.CAFE_ID);
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
        assertThat(cafe.getId()).isEqualTo(CafeService.CAFE_ID);
        verify(cafeRepository).findById(CafeService.CAFE_ID);
        verify(cafeRepository).save(argThat(arg -> arg.getId().equals(CafeService.CAFE_ID)));
    }

    @Test
    void testOpenCafe() {
        // GIVEN
        when(cafeRepository.findById(CafeService.CAFE_ID)).thenReturn(Optional.of(cafe));
        when(requester.hasAuthorityTo(cafe)).thenReturn(true);
        when(cafeRepository.save(cafe)).thenReturn(cafe);

        // WHEN
        cafeService.openCafe(requester);

        // THEN
        verify(requester).hasAuthorityTo(cafe);
        verify(cafeRepository).findById(CafeService.CAFE_ID);
        verify(cafeRepository).save(argThat(arg -> arg.equals(cafe) && arg.getIsOpen()));
    }

    @Test
    void testOpenCafeNoAuthority() {
        // GIVEN
        String requesterName = "test@example.com";
        when(cafeRepository.findById(CafeService.CAFE_ID)).thenReturn(Optional.of(cafe));
        when(requester.hasAuthorityTo(cafe)).thenReturn(false);
        when(requester.getEmailAddress()).thenReturn(new EmailAddress(requesterName));
        when(cafeRepository.save(cafe)).thenReturn(cafe);

        // WHEN
        Throwable throwable = catchThrowable(() -> cafeService.openCafe(requester));

        // THEN
        verify(cafeRepository).findById(CafeService.CAFE_ID);
        verify(requester).hasAuthorityTo(cafe);
        verify(cafeRepository, never()).save(any());
        assertThat(throwable).isInstanceOf(CafeService.NoAuthorityException.class).hasMessageContaining(requesterName);
    }

    @Test
    void testCloseCafe() {
        // GIVEN
        when(cafeRepository.findById(CafeService.CAFE_ID)).thenReturn(Optional.of(cafe));
        when(requester.hasAuthorityTo(cafe)).thenReturn(true);
        when(cafeRepository.save(cafe)).thenReturn(cafe);

        // WHEN
        cafeService.closeCafe(requester);

        // THEN
        verify(requester).hasAuthorityTo(cafe);
        verify(cafeRepository).findById(CafeService.CAFE_ID);
        verify(cafeRepository).save(cafe);
        verify(cafeRepository).save(argThat(arg -> arg.equals(cafe) && !arg.getIsOpen()));
    }

    @Test
    void testCloseCafeNoAuthority() {
        // GIVEN
        String requesterName = "test@example.com";
        when(cafeRepository.findById(CafeService.CAFE_ID)).thenReturn(Optional.of(cafe));
        when(requester.hasAuthorityTo(cafe)).thenReturn(false);
        when(requester.getEmailAddress()).thenReturn(new EmailAddress(requesterName));
        when(cafeRepository.save(cafe)).thenReturn(cafe);

        // WHEN
        Throwable throwable = catchThrowable(() -> cafeService.closeCafe(requester));

        // THEN
        verify(cafeRepository).findById(CafeService.CAFE_ID);
        verify(requester).hasAuthorityTo(cafe);
        verify(cafeRepository, never()).save(any());
        assertThat(throwable).isInstanceOf(CafeService.NoAuthorityException.class).hasMessageContaining(requesterName);
    }
}