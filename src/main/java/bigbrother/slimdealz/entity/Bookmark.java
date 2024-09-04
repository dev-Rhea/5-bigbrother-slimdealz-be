package bigbrother.slimdealz.entity;

import bigbrother.slimdealz.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "bookmarks")
@Getter
/*
 * entity 에서 setter는 JPA의 update기능을 수행하기 때문에,
 * setter를 통해 값을 생성하려는 건지, 수정하려는건지에 대한 의도가 모호해짐.
 * 또한, 의도치 않게 값이 변경되는 경우가 발생할 수 있으므로, setter를 사용하지 않는 것이 좋습니다.
 *
 */
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;  // Reference to the Member entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // Reference to the Product entity

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;
}
