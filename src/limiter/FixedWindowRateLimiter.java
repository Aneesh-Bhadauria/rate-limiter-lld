package limiter;

import enums.RateLimitType;
import model.RateLimitConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class FixedWindowRateLimiter extends RateLimiter{
    private final Map<String , Integer> requestCount = new ConcurrentHashMap<>();
    private final Map<String , Long> windowStart = new HashMap<>();

    public FixedWindowRateLimiter(RateLimitConfig config){
        super(config , RateLimitType.FIXED_WINDOW);
    }

    @Override
    public boolean allowRequest(String userId) {
        AtomicBoolean allowed = new AtomicBoolean(false);

        long currentReqWindow = System.currentTimeMillis() / 1000 / config.getWindowInSeconds();

        requestCount.compute(userId , (id ,count) -> {
            long lastReqWindow = windowStart.getOrDefault(id , currentReqWindow);

            if(lastReqWindow != currentReqWindow){
                windowStart.put(id , currentReqWindow);
                allowed.set(true);
                return 1;
            }
           if(count == null) count = 0;

           if(count < config.getMaxRequest()){
               allowed.set(true);
               return count + 1;
           }

           return count;
        });

        return allowed.get();
    }
}
