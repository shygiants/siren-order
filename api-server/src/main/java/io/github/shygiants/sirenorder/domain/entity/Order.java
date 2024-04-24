package io.github.shygiants.sirenorder.domain.entity;

import io.github.shygiants.sirenorder.domain.enumerate.OrderState;
import io.github.shygiants.sirenorder.domain.valueobject.CoffeeQuantity;
import io.github.shygiants.sirenorder.utils.CoffeeQuantityConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "createdAt")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @Convert(converter = CoffeeQuantityConverter.class)
    private CoffeeQuantity coffeeQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAFE_ID")
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Member customer;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @CreatedDate
    private LocalDateTime createdAt;

    public Order(CoffeeQuantity coffeeQuantity, Cafe cafe, Member customer) {
        this.coffeeQuantity = coffeeQuantity;
        this.cafe = cafe;
        this.customer = customer;
        state = OrderState.PENDING;
    }
}
