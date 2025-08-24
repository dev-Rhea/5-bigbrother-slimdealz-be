package bigbrother.slimdealz.dto.product;

import bigbrother.slimdealz.entity.product.PriceHistory;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PriceHistoryDto (
    Long id,
    int previousPrice,
    Long priceId,
    int currentPrice,
    Long VendorId,
    String vendorName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PriceHistoryDto from(PriceHistory priceHistory) {
        return PriceHistoryDto.builder()
            .id(priceHistory.getId())
            .previousPrice(priceHistory.getPreviousPrice())
            .priceId(priceHistory.getPrice().getId())
            .currentPrice(priceHistory.getPrice().getDiscountPrice())
            .VendorId(priceHistory.getPrice().getVendor().getId())
            .vendorName(priceHistory.getPrice().getVendor().getVendorName())
            .createdAt(priceHistory.getCreatedAt())
            .updatedAt(priceHistory.getUpdatedAt())
            .build();
    }
}
