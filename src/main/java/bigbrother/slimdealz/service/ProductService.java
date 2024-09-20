package bigbrother.slimdealz.service;

import bigbrother.slimdealz.dto.product.ProductConverter;
import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
import bigbrother.slimdealz.repository.Product.ProductRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final S3Service s3Service;

    // 상품 검색
    @Transactional
    public List<ProductDto> searchProducts(String keyword, Long lastSeenId, int size) {
        List<ProductDto> products = productRepository.searchByKeyword(keyword, lastSeenId, size)
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getName());
                    productDto.setImageUrl(imageUrl);
                    return productDto;
                }) //converter 를 통해 DTO 로 변환
                .collect(Collectors.toList()); // stream의 변환된 요소들을 리스트로 반환

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    // 오늘의 최저가 상품 목록
    @Transactional
    public List<ProductDto> findLowestPriceProducts() {
        List<ProductDto> products = productRepository.findLowestPriceProducts()
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getName());
                    productDto.setImageUrl(imageUrl);
                    return productDto;
                })
                .collect(Collectors.toList());

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    // 상품 상세 페이지 정보
    @Transactional
    public ProductDto getProductWithLowestPriceByName(String productName, HttpServletRequest request, HttpServletResponse response) {
        Product product = productRepository.findProductWithLowestPriceByName(productName);

        if(product == null) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }

        Cookie[] cookies = request.getCookies();
        Cookie oldCookie = findCookie(cookies, "view_count");

        if(oldCookie != null) {
            if(!oldCookie.getValue().contains(product.getName())) {
                oldCookie.setValue(oldCookie.getValue() + product.getId());
                oldCookie.setPath("/");
                response.addCookie(oldCookie);
                product.addViewCount();
                productRepository.save(product);
            }
            else {
                Cookie newCookie = new Cookie("view_count", product.getName());
                newCookie.setPath("/");
                response.addCookie(oldCookie);
                product.addViewCount();
                productRepository.save(product);
            }
        }

        ProductDto productDto = ProductConverter.toProductDTO(product);

        String imageUrl = s3Service.getProductImageUrl(productName);

        productDto.setImageUrl(imageUrl);

        return productDto;
    }

    // 카테고리 별 상품 조회
    @Transactional
    public List<ProductDto> findByCategory(String category, Long lastSeenId, int size) {
        List<ProductDto> products = productRepository.findByCategory(category, lastSeenId, size)
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getName());
                    productDto.setImageUrl(imageUrl);
                    return productDto;
                })
                .collect(Collectors.toList());

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    // 판매처 리스트
    @Transactional
    public List<ProductDto> getProductWithVendors(String productName) {
        List<Product> products = productRepository.findProductWithVendors(productName);

        return products.stream()
                .map(ProductConverter::toProductDTO)
                .collect(Collectors.toList());
    }

    // 랜덤 추천
    @Transactional
    public  List<ProductDto> findRandomProducts() {
        List<ProductDto> products = productRepository.findRandomProducts()
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getName());
                    productDto.setImageUrl(imageUrl);
                    return productDto;
                })
                .collect(Collectors.toList());

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    private Cookie findCookie(Cookie[] cookies, String name) {
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    return c;
                }
            }
        }
        return null;
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
    }

    public Cookie addViewCount(Long productId) {
        Product product = getProductById(productId);
        product.addViewCount();
        productRepository.save(product);
        return null;
    }

    // 인기 급상승
    public List<ProductDto> getPopularProducts(LocalDateTime localDateTime) {
        List<ProductDto> popularProducts = productRepository.findPopularProducts(localDateTime);
        List<ProductDto> adjustedPopularProducts = adjustScoreProducts(popularProducts, localDateTime);

        // 조회수가 모두 0인 경우 가격 높은 순으로 반환
        if(adjustedPopularProducts.isEmpty() || allViewCountZero(adjustedPopularProducts)){
            productRepository.findPopularProducts(localDateTime);
        }

        return adjustedPopularProducts;
    }

    private boolean allViewCountZero(List<ProductDto> adjustedPopularProducts) {
        return adjustedPopularProducts.stream().allMatch(products -> products.getViewCount() == 0);
    }

    // 인기 급상승 점수 계산 로직
    private List<ProductDto> adjustScoreProducts(List<ProductDto> popularProducts, LocalDateTime previousTime) {
        List<ProductDto> previousPopularProducts = productRepository.findPopularProducts(previousTime);

        for(ProductDto p : popularProducts) {
            boolean wasInPopular = previousPopularProducts.stream().anyMatch(prev -> prev.getId().equals(p.getId()));

            if(wasInPopular) {
                p.setScore(p.getScore() - 1);

            }
            else {
                p.setScore(p.getScore() + 1);
            }
        }
        return popularProducts;
    }

}