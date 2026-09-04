package factory;

import enums.RateLimitType;
import limiter.FixedWindowRateLimiter;
import limiter.RateLimiter;
import limiter.SlidingWindowLogRateLimiter;
import limiter.TokenBucketRateLimiter;
import model.RateLimitConfig;

public class RateLimiterFactory {
   public static RateLimiter createRateLimiter(RateLimitType algo , RateLimitConfig config){
       return switch (algo){
           case TOKEN_BUCKET -> new TokenBucketRateLimiter(config);
           case FIXED_WINDOW -> new FixedWindowRateLimiter(config);
           case SLIDING_WINDOW_LOG -> new SlidingWindowLogRateLimiter(config);
           default -> throw new IllegalArgumentException("Unknown algo" + algo);
       };
   }
}
