package bigbrother.slimdealz.cache.key;

import java.time.Duration;

public class CacheKeys {

    private CacheKeys() {
        throw new IllegalStateException("Utility class");
    }

    public static final String PREFIX = "slimdealz";

    // 상품
    public static final String LOWEST_PRICE_PRODUCTS = PREFIX + "product:lowest_price";
    public static final String POPULAR_PRODUCTS = PREFIX + "product:popular";
    public static final String RANDOM_PRODUCTS = PREFIX + "product:random";
    public static final String TOP_PRICE_PRODUCTS = PREFIX + "product:top_price";

    // TTL
    public static final Duration DEFAULT_TTL = Duration.ofHours(25); // 25시간
    public static final Duration SHORT_TTL = Duration.ofMinutes(30);  // 30분
    public static final Duration LONG_TTL = Duration.ofDays(7);       // 7일

    public static String getProductDetailKey(Long productId) {
        return PREFIX + "product:detail:" + productId;
    }

    public static String getProductVendorsKey(String productName) {
        return PREFIX + "product:vendors:" + productName.replaceAll("\\s+", "_");
    }

    public static String getPriceHistoryKey(Long priceId) {
        return PREFIX + "price:history:" + priceId;
    }

    // 캐시 상태 모니터링 키
    public static final String CACHE_HEALTH_KEY = PREFIX + "health:check";
    public static final String CACHE_METRICS_KEY = PREFIX + "metrics:";
}
