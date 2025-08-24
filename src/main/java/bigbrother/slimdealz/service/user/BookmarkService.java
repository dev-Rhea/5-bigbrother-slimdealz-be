package bigbrother.slimdealz.service.user;

import bigbrother.slimdealz.dto.product.PriceDto;
import bigbrother.slimdealz.dto.product.ProductConverter;
import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.dto.product.VendorDto;
import bigbrother.slimdealz.dto.user.BookmarkDto;
import bigbrother.slimdealz.dto.user.BookmarkProductPriceDto;
import bigbrother.slimdealz.entity.user.Bookmark;
import bigbrother.slimdealz.entity.user.Member;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.repository.product.ProductRepository;
import bigbrother.slimdealz.repository.user.BookmarkRepository;
import bigbrother.slimdealz.repository.user.MemberRepository;
import bigbrother.slimdealz.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final S3Service s3Service;

    // Bookmark 엔티티를 BookmarkDto로 변환하는 메서드
    private BookmarkDto convertToBookmarkDto(Bookmark bookmark) {
        return BookmarkDto.builder()
                .id(bookmark.getId())
                .userId(bookmark.getMember().getId())
                .productId(bookmark.getProduct().getId())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }

    // 유저가 특정 상품을 북마크했는지 확인하는 메서드
    public boolean isProductBookmarked(Long userId, String productName) {
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByMemberIdAndProductName(userId, productName);
        return !bookmarks.isEmpty();
    }

    // 유저의 북마크 리스트를 가져오는 메서드
    public List<BookmarkProductPriceDto> getUserBookmarksWithPrice(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksWithProductsAndPrices(userId);
        return bookmarks.stream().map(bookmark -> {
            List<PriceDto> prices = bookmark.getProduct().getPrices().stream()
                    .map(price -> PriceDto.builder()
                            .id(price.getId())
                            .setPrice(price.getSetPrice())
                            .promotion(price.getPromotion())
                            .productId(price.getProduct().getId())
                            .vendor(VendorDto.builder()
                                    .id(price.getVendor().getId())
                                    .vendorName(price.getVendor().getVendorName())
                                    .build())
                            .build())
                    .collect(Collectors.toList());

            return BookmarkProductPriceDto.builder()
                    .bookmarkId(bookmark.getId())
                    .productId(bookmark.getProduct().getId())
                    .productName(bookmark.getProduct().getProductName())  // Product 엔티티에서 productName 가져옴
                    .shippingFee(bookmark.getProduct().getShippingFee())
                    .prices(prices)
                    .image(s3Service.getProductImageUrl(bookmark.getProduct().getProductName()))
                    .build();
        }).collect(Collectors.toList());
    }

    // 상품 이름으로 북마크 추가하는 메서드 (최저가 상품을 선택)
    public BookmarkDto addBookmarkByProductName(Long userId, String productName) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 상품 이름으로 모든 상품을 검색하고, 최저가 상품을 선택
        Product cheapestProduct = productRepository.findByProductName(productName).stream()
                .min(Comparator.comparing(product -> product.getPrices().stream()
                        .min(Comparator.comparing(price -> price.getSetPrice()))
                        .orElseThrow(() -> new RuntimeException("Price not found")).getSetPrice()))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 이미 해당 상품이 북마크되어 있는지 확인
        if (bookmarkRepository.findByMemberAndProduct(member, cheapestProduct).isPresent()) {
            throw new RuntimeException("This product is already bookmarked.");
        }

        // 북마크 생성
        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .product(cheapestProduct)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return convertToBookmarkDto(savedBookmark);
    }

    // 상품 이름으로 북마크 삭제하는 메서드 (최저가 상품을 선택)
    public void removeBookmarkByProductName(Long userId, String productName) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 상품 이름으로 최저가 상품을 선택
        Product cheapestProduct = productRepository.findByProductName(productName).stream()
                .min(Comparator.comparing(product -> product.getPrices().stream()
                        .min(Comparator.comparing(price -> price.getSetPrice()))
                        .orElseThrow(() -> new RuntimeException("Price not found")).getSetPrice()))
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Bookmark bookmark = bookmarkRepository.findByMemberAndProduct(member, cheapestProduct)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        bookmarkRepository.delete(bookmark);
    }

    // 북마크 기반 추천 상품
    @Transactional(readOnly = true)
    public List<ProductDto> getRecommendedBookmarkProducts(Long userId) {
        List<Product> recommendedProducts = bookmarkRepository.findRecommendedProducts(userId);

        return recommendedProducts.stream()
                .map(ProductConverter::toProductDTO)
                .collect(Collectors.toList());
    }
}
