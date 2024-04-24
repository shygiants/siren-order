package io.github.shygiants.sirenorder.domain.repository;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.entity.Member;
import io.github.shygiants.sirenorder.domain.entity.Order;
import io.github.shygiants.sirenorder.domain.service.CafeService;
import io.github.shygiants.sirenorder.domain.service.MemberService;
import io.github.shygiants.sirenorder.domain.valueobject.CoffeeQuantity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CafeService cafeService;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void testSaveAndFindById() {
        Cafe cafe = cafeService.getCafe();
        Long customerMemberId = memberService.createCustomer("test@example.com", "password");
        entityManager.flush();

        LocalDateTime beforeCreate = LocalDateTime.now();
        Member customer = memberRepository.findById(customerMemberId).orElseThrow();
        Order order = new Order(new CoffeeQuantity(1), cafe, customer);

        Order saved = orderRepository.save(order);
        entityManager.flush();

        assertThat(saved).isEqualTo(order);
        assertThat(saved.getCreatedAt()).isBetween(beforeCreate, LocalDateTime.now());

        entityManager.clear();
        entityManager.close();

        Optional<Order> orderOptional = orderRepository.findById(saved.getId());
        assertThat(orderOptional).hasValue(order);
    }
}