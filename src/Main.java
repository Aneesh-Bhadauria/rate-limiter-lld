import enums.UserTier;
import model.User;
import service.RateLimitService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        RateLimitService rateLimitService = new RateLimitService();

        User freeUser = new User("user1" , UserTier.FREE);
        User premiumUser = new User("user2" , UserTier.PREMIUM);

        checkConcurrency(rateLimitService);
    }

    static void checkConcurrency(RateLimitService rateLimitService) throws InterruptedException {
        User freeUser1 = new User("user1" , UserTier.FREE);

        int threads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        CyclicBarrier barrier = new CyclicBarrier(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for(int i = 1 ; i <= threads ; i++){
            final int reqNum = i;
            executor.submit(() -> {
                try{
                    barrier.await();
                }catch (Exception e){
                    e.printStackTrace();
                }

                boolean allowed = rateLimitService.allowRequest(freeUser1);
                System.out.println();

                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
    }
}
