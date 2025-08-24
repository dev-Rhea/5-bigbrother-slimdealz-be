package bigbrother.slimdealz.dto.product;

import bigbrother.slimdealz.entity.product.Price;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PriceDto (
    long id,
    int setPrice,
    int discountPrice,
    long productId,
    String productName,
    long vendorId,
    String vendorName,
    String vendorUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PriceDto from(Price price) {
        return PriceDto.builder()
            .id(price.getId())
            .setPrice(price.getSetPrice())
            .discountPrice(price.getDiscountPrice())
            .productId(price.getProduct().getId())
            .productName(price.getProduct().getProductName())
            .vendorId(price.getVendor().getId())
            .vendorName(price.getVendor().getVendorName())
            .vendorUrl(price.getVendor().getVendorUrl())
            .updatedAt(price.getUpdatedAt())
            .build();
    }
}