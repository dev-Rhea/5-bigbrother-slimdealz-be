package bigbrother.slimdealz.dto.product;

import bigbrother.slimdealz.entity.product.Price;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
public record ChartDto(
    Long priceId,
    int lowestPrice,
    Long productId,
    String productName,
    LocalDateTime updatedAt
) {
    public static ChartDto of(Price price) {
        return ChartDto.builder()
            .priceId(price.getId())
            .lowestPrice(price.getDiscountPrice())
            .productId(price.getProduct().getId())
            .productName(price.getProduct().getProductName())
            .updatedAt(price.getUpdatedAt())
            .build();
    }
}