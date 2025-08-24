package bigbrother.slimdealz.dto.product;

import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.entity.product.Vendor;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ProductDto(
    Long id,
    String productName,
    String category,
    int shippingFee,
    double productRating,
    int viewCount,
    Integer score,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<PriceDto> prices,
    List<String> vendors
) {
    public static ProductDto from(Product product) {
        return ProductDto.builder()
            .id(product.getId())
            .productName(product.getProductName())
            .category(product.getCategory())
            .shippingFee(product.getShippingFee())
            .productRating(product.getProductRating())
            .viewCount(product.getViewCount())
            .score(product.getScore())
            .createdAt(product.getCreatedAt())
            .updatedAt(product.updatedAt())
            .prices(product.getPrices().stream()
                .map(PriceDto::from)
                .toList())
            .vendors(product.getVendors().stream()
                .map(Vendor::getVendorName)
                .toList())
            .build();
    }
}

