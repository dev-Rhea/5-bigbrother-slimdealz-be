//package bigbrother.slimdealz.dto.product;
//
//import bigbrother.slimdealz.entity.product.Price;
//import bigbrother.slimdealz.entity.product.Product;
//import bigbrother.slimdealz.entity.product.Vendor;
//
//import java.util.stream.Collectors;
//import lombok.Builder;
//
//@Builder
//public class ProductConverter {
//    public static ProductDto toProductDTO(Product product) {
//        return ProductDto.builder()
//                .id(product.getId())
//                .productName(product.getProductName())
//                .category(product.getCategory())
//                .shippingFee(product.getShippingFee())
//                .vendorUrl(product.getVendorUrl())
//                .viewCount(product.getViewCount())
//                .viewedAt(product.getViewedAt())
//                .prices(product.getPrices().stream()
//                        .map(ProductConverter::toPriceDTO)
//                        .collect(Collectors.toList()))
//                .build();
//    }
//
//    public static PriceDto toPriceDTO(Price price) {
//        return PriceDto.builder()
//                .id(price.getId())
//                .setPrice(price.getSetPrice())
//                .promotion(price.getPromotion())
//                .productId(price.getProduct().getId())
//                .vendor(toVendorDTO(price.getVendor()))
//                .createdAt(price.getCreatedAt())
//                .updatedAt(price.getUpdatedAt())
//                .build();
//    }
//
//    public static VendorDto toVendorDTO(Vendor vendor) {
//        return VendorDto.builder()
//                .id(vendor.getId())
//                .vendorName(vendor.getVendorName())
//                .vendorUrl(vendor.getVendorUrl())
//                .build();
//    }
//}