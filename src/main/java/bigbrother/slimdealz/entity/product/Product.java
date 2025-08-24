package bigbrother.slimdealz.entity.product;

import bigbrother.slimdealz.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.util.stream.Collectors;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "products")
@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_category", nullable = false)
    private String category;

    @Column(name = "shipping_fee", nullable = false)
    private int shippingFee;

    @Column(name = "product_rating")
    private double productRating;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "score")
    private Integer score;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Price> prices;

    // 조회수 추가
    public void incrementViewCount() {
        this.viewCount++;
    }

    // 점수 추가
    public void adjustScore(int delta) {
        if(this.score == null) {
            this.score = 0;
        }
        this.score += delta;
    }

    public List<Vendor> getVendors() {
        return prices.stream()
            .map(Price::getVendor)
            .distinct()
            .collect(Collectors.toList());
    }
}
