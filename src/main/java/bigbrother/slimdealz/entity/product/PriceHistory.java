package bigbrother.slimdealz.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "priceHistory")  // 테이블 이름을 대소문자에 맞춰 사용
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "previous_price")
    private int previousPrice;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @ManyToOne
    @JoinColumn(name = "price_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PriceHistory_Price"))
    private Price price;
}
