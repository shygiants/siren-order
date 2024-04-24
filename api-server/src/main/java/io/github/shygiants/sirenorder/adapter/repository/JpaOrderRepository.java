package io.github.shygiants.sirenorder.adapter.repository;

import io.github.shygiants.sirenorder.domain.entity.Order;
import io.github.shygiants.sirenorder.domain.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends OrderRepository, JpaRepository<Order, Long> {
}
