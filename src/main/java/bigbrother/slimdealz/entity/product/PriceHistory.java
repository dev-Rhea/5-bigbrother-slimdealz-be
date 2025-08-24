package bigbrother.slimdealz.entity.product;

import bigbrother.slimdealz.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "price_history")  // 테이블 이름을 대소문자에 맞춰 사용
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PriceHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "previous_price", nullable = false)
    private int previousPrice;

    @ManyToOne
    @JoinColumn(name = "price_id", nullable = false)
    private Price price;
}
