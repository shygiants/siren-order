package io.github.shygiants.sirenorder.domain.repository;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
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

        assertThat(saved).isEqualTo(cafe);

        entityManager.clear();
        entityManager.close();

        Optional<Cafe> cafeFoundOptional = cafeRepository.findById(CAFE_ID);
        assertThat(cafeFoundOptional).hasValue(cafe);
    }
}