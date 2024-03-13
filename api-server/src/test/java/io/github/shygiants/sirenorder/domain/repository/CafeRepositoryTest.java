package io.github.shygiants.sirenorder.domain.repository;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@SpringBootTest
@Transactional
class CafeRepositoryTest {

    @Autowired
    CafeRepository cafeRepository;
    @Autowired
    EntityManager entityManager;
    final static Long CAFE_ID = 1L;


    @Test
    void testSaveAndFindById() {
        Cafe cafe = Cafe.fromId(CAFE_ID);
        Cafe saved = cafeRepository.save(cafe);
        entityManager.flush();

        Assertions.assertThat(saved.getId()).isEqualTo(cafe.getId());
        Assertions.assertThat(saved.getOpen()).isEqualTo(cafe.getOpen());

        entityManager.clear();
        entityManager.close();

        Optional<Cafe> cafeFoundOptional = cafeRepository.findById(CAFE_ID);
        Assertions.assertThat(cafeFoundOptional).isPresent();

        Cafe cafeFound = cafeFoundOptional.get();
        Assertions.assertThat(cafeFound.getId()).isEqualTo(cafe.getId());
        Assertions.assertThat(cafeFound.getOpen()).isEqualTo(cafe.getOpen());
    }
}