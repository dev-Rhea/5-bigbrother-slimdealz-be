package bigbrother.slimdealz.cache.cofig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CacheMonitoringConfig {

    private final RedisTemplate<String, Object> redisTemplate;

    @Bean
    public HealthIndicator redisHealthIndicator() {
        return () -> {
            try {
                redisTemplate.hasKey("health:check");
                return Health.up()
                    .withDetail("redis", "Redis connection is healthy")
                    .build();
            } catch (Exception e) {
                log.error("Redis health check failed", e);
                return Health.down()
                    .withDetail("redis", "Redis connection failed")
                    .withException(e)
                    .build();
            }
        };
    }
}
