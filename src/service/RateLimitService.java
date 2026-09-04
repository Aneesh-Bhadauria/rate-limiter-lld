package service;

import enums.RateLimitType;
import enums.UserTier;
import factory.RateLimiterFactory;
import limiter.RateLimiter;
import model.RateLimitConfig;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class RateLimitService {
    private final Map<UserTier , RateLimiter> rateLimiters = new HashMap<>();

    public RateLimitService(){
        rateLimiters.put(
                UserTier.FREE,
                RateLimiterFactory.createRateLimiter(
                        RateLimitType.TOKEN_BUCKET,
                        new RateLimitConfig(10, 60) // 10 req/min
                )
        );

        rateLimiters.put(
                UserTier.PREMIUM,
                RateLimiterFactory.createRateLimiter(
                        RateLimitType.FIXED_WINDOW,
                        new RateLimitConfig(100, 60)
                )
        );
    }

    public boolean allowRequest(User user){
        RateLimiter rateLimiter = rateLimiters.get(user.getTier());
        if(rateLimiter == null)
            throw new IllegalArgumentException("No limiter configured for tier: " + user.getTier());

        return rateLimiter.allowRequest(user.getUserId());
    }

}
