package io.github.shygiants.sirenorder.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class CafeTest {

    @Test
    void testFromId() {
        // GIVEN
        Long CAFE_ID = 1L;

        // WHEN
        Cafe cafe = Cafe.fromId(CAFE_ID);

        // THEN
        assertThat(cafe.getId()).isEqualTo(CAFE_ID);
        assertThat(cafe.getOpen()).isFalse();
    }
}