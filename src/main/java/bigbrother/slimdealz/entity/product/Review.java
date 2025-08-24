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
    Long id;

    @Column(name = "product_name", nullable = false)
    String productName;

    @Column(name = "customer_rating", nullable = false)
    int customerRating;

    @Column(name = "content", nullable = false)
    String content;

    @Column(name = "product_source", nullable = false)
    String productSource;

    @Column(name = "user_name", nullable = false)
    String userName;

}
