package bigbrother.slimdealz.entity.product;

import bigbrother.slimdealz.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "prices")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "set_price", nullable = false)
    private int setPrice;

    @Column(name = "discount_price")
    private int discountPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
}