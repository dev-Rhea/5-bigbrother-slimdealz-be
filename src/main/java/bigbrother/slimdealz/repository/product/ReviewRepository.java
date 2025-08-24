package bigbrother.slimdealz.repository.product;

import bigbrother.slimdealz.dto.product.ReviewDto;
import bigbrother.slimdealz.entity.product.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r.id FROM Review r WHERE r.product.id = :productId")
    List<ReviewDto> findReviewsByproductId(@Param("productId") Long productId);
}
