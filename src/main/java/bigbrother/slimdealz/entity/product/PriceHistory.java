package bigbrother.slimdealz.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "priceHistory")  // 테이블 이름을 대소문자에 맞춰 사용
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "previous_price")
    int previousPrice;

    @Column(name = "updated_at")
    Instant updatedAt;

    @Column(name = "end_at")
    Instant endAt;

    @ManyToOne
    @JoinColumn(name = "price_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PriceHistory_Price"))
    private Price price;
}
