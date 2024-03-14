package io.github.shygiants.sirenorder.adapter.repository;

import io.github.shygiants.sirenorder.domain.entity.Cafe;
import io.github.shygiants.sirenorder.domain.repository.CafeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCafeRepository extends CafeRepository, JpaRepository<Cafe, Long> {
}
