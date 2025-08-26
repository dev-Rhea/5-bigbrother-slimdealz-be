package bigbrother.slimdealz.cache.service;

import bigbrother.slimdealz.cache.key.CacheKeys;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheMonitoringService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final Map<String, AtomicLong> cacheHits = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> cacheMisses = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> cacheErrors = new ConcurrentHashMap<>();
    private final Map<String, Long> lastRefreshTimes = new ConcurrentHashMap<>();

    /**
     * 캐시 히트 기록
     */
    public void recordCacheHit(String cacheKey) {
        cacheHits.computeIfAbsent(cacheKey, k -> new AtomicLong(0)).incrementAndGet();
        log.debug("Cache hit recorded for key: {}", cacheKey);
    }

    /**
     * 캐시 미스 기록
     */
    public void recordCacheMiss(String cacheKey) {
        cacheMisses.computeIfAbsent(cacheKey, k -> new AtomicLong(0)).incrementAndGet();
        log.debug("Cache miss recorded for key: {}", cacheKey);
    }

    /**
     * 캐시 오류 기록
     */
    public void recordCacheError(String cacheKey, String errorMessage) {
        cacheErrors.computeIfAbsent(cacheKey, k -> new AtomicLong(0)).incrementAndGet();
        log.error("Cache error recorded for key: {}, Error: {}", cacheKey, errorMessage);
    }

    /**
     * 캐시 갱신 기록
     */
    public void recordCacheRefresh(String cacheKey, int itemCount) {
        lastRefreshTimes.put(cacheKey, System.currentTimeMillis());
        log.info("Cache refreshed for key: {} with {} items at {}", cacheKey, itemCount, LocalDateTime.now());
    }

    /**
     * 캐시 통계 조회
     */
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> stats = new HashMap<>();

        for(String key : cacheHits.keySet()) {
            long hits = cacheHits.get(key).get();
            long misses = cacheMisses.getOrDefault(key, new AtomicLong(0)).get();
            long total = hits + misses;

            if(total > 0) {
                double hitRate = (double) hits / total * 100;
                stats.put((key + ":hitRate"), String.format("%.2f%%", hitRate));
                stats.put(key + "totalRequests", total);
                stats.put((key + ":hits"), hits);
                stats.put((key + ":misses"), misses);
            }
        }
        stats.put("lastRefreshTimes", lastRefreshTimes);
        stats.put("cacheErrors", cacheErrors);
        return stats;
    }

    /**
     * Redis 연결 상태 확인
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void checkRedisConnection() {
        try {
            redisTemplate.opsForValue().set(CacheKeys.CACHE_HEALTH_KEY, "OK", CacheKeys.SHORT_TTL);
            String healthStatus = (String) redisTemplate.opsForValue().get(CacheKeys.CACHE_HEALTH_KEY);

            if("OK".equals(healthStatus)) {
                log.info("Redis connection is healthy");
            } else {
                log.warn("Unexpected Redis health status: {}", healthStatus);
            }
        } catch (Exception e) {
            log.error("Redis connection check failed", e);
            recordCacheError("RedisConnection", e.getMessage());
        }
    }

    /**
     * 캐시 메트릭스를 Redis에 저장
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void persistCacheMetrics() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm"));
            String metricsKey = CacheKeys.CACHE_METRICS_KEY + timestamp;

            Map<String, Object> metrics = getCacheStatistics();
            redisTemplate.opsForValue().set(metricsKey, metrics, CacheKeys.LONG_TTL);

            log.debug("Cache metrics persisted for timestamp: {}", timestamp);

        } catch (Exception e) {
            log.error("Failed to persist cache metrics", e);
        }
    }

    /**
     * 캐시 클리어 (관리용)
     */
    public void clearCache(String pattern) {
        try {
            redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys(pattern)));
            log.info("Cleared cache with pattern: {}", pattern);
        } catch (Exception e) {
            log.error("Failed to clear cache with pattern: {}", pattern, e);
        }
    }

    /**
     * 모든 상품 관련 캐시 클리어
     */
    public void clearAllProductCaches() {
        clearCache(CacheKeys.PREFIX + "product:*");
        log.info("All product caches cleared");
    }
}
