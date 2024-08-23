package bigbrother.slimdealz.entity.product;


import bigbrother.slimdealz.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "products")
@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "product_name", nullable = false)
    String name;

    @Column(name = "product_category", nullable = false)
    String category;

    @Column(name = "shipping_price")
    String shippingFee;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Price> prices;

}
