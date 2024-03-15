package io.github.shygiants.sirenorder.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cafe {
    @Id
    private Long id;
    private Boolean open;

    public static Cafe fromId(Long id) {
        Cafe cafe = new Cafe();
        cafe.id = id;
        cafe.open = false;
        return cafe;
    }
}
