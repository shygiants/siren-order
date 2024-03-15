package io.github.shygiants.sirenorder.domain.repository;

import io.github.shygiants.sirenorder.domain.entity.Cafe;

import java.util.Optional;

public interface CafeRepository {

    Cafe save(Cafe cafe);

    Optional<Cafe> findById(Long id);
}
