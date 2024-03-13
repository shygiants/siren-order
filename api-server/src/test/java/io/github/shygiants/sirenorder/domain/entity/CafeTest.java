package io.github.shygiants.sirenorder.domain.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class CafeTest {

    @Test
    void testFromId() {
        // GIVEN
        Long CAFE_ID = 1L;

        // WHEN
        Cafe cafe = Cafe.fromId(CAFE_ID);

        // THEN
        Assertions.assertThat(cafe.getId()).isEqualTo(CAFE_ID);
        Assertions.assertThat(cafe.getOpen()).isFalse();
    }
}