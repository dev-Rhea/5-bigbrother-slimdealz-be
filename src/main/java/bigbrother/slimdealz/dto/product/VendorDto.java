package bigbrother.slimdealz.dto.product;

import bigbrother.slimdealz.entity.product.Vendor;
import lombok.Builder;

@Builder
public record VendorDto (
    Long vendorId,
    String vendorName,
    String vendorUrl
) {
    public static VendorDto from(Vendor vendor) {
        return VendorDto.builder()
                .vendorId(vendor.getId())
                .vendorName(vendor.getVendorName())
                .vendorUrl(vendor.getVendorUrl())
                .build();
    }
}
