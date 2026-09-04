package limiter;

import enums.RateLimitType;
import lombok.AllArgsConstructor;
import model.RateLimitConfig;

@AllArgsConstructor
public abstract class RateLimiter {
    protected final RateLimitConfig config;
    protected final RateLimitType type;

    public abstract boolean allowRequest(String userId);
}
