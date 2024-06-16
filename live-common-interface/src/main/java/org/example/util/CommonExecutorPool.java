package org.example.util;

import java.util.concurrent.*;

public class CommonExecutorPool {

    public static Executor msgExecutorPool = new ThreadPoolExecutor(3, 6, 60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"msg-"+ThreadLocalRandom.current().nextInt(6));
        }
    }, new ThreadPoolExecutor.AbortPolicy());

}
