package ihep.ac.cn.factory;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import ihep.ac.cn.enmus.BlockingQueueEnum;
import ihep.ac.cn.enmus.RejectedExecutionHandlerEnum;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CAThreadFactory {

    private static final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();

    private static final ExecutorService defaultExecutorService = new ThreadPoolExecutor(
            3,
            30,
            5L,
            TimeUnit.MILLISECONDS,
            BlockingQueueEnum.ARRAY.queue(),
            threadFactoryBuilder.build(),
            RejectedExecutionHandlerEnum.CALLER_RUNS.handler());

    private static java.util.concurrent.ThreadFactory defaultThreadFactory() {
        return threadFactoryBuilder.build();
    }

    public static java.util.concurrent.ThreadFactory threadFactorySetName(String name) {
        return threadFactoryBuilder.setNameFormat(name + "-%d").build();
    }

    public static ExecutorService defaultExecutorService() {
        return defaultExecutorService;
    }

    public static ExecutorService executorServiceCustomer(int corPoolSize, int maxPoolSize, long keepAliveTime, java.util.concurrent.ThreadFactory threadFactory, BlockingQueueEnum blockingQueueEnum, RejectedExecutionHandlerEnum rejectedExecutionHandlerEnum) {
        return new ThreadPoolExecutor(
                corPoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                blockingQueueEnum.queue(corPoolSize * maxPoolSize * 10),
                threadFactory,
                rejectedExecutionHandlerEnum.handler()
        );
    }

}