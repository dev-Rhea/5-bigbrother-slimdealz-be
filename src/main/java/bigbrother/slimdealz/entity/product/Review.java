package bigbrother.slimdealz.entity.product;

import bigbrother.slimdealz.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "reviews")
@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_rating", nullable = false)
    private int customerRating;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "product_source", nullable = false)
    private String productSource;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
