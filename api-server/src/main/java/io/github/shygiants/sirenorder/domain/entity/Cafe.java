package io.github.shygiants.sirenorder.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(exclude = {"owner"})
public class Cafe {
    @Id
    private Long id;

    private Boolean isOpen;

    @OneToOne(mappedBy = "cafe")
    private Member owner;

    public static Cafe fromId(Long id) {
        Cafe cafe = new Cafe();
        cafe.id = id;
        cafe.isOpen = false;
        return cafe;
    }

    public void open() {
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }
}
