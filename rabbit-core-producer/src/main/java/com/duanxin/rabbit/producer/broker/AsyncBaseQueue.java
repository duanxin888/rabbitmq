package com.duanxin.rabbit.producer.broker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 异步消息队列
 * @author duanxin
 * @version 1.0
 * @date 2020/4/21 9:14
 */
public class AsyncBaseQueue {

    private static final Logger log = LoggerFactory.getLogger(AsyncBaseQueue.class.getName());

    private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();

    private static final int QUEUE_SIZE = 10000;

    private static ExecutorService senderAsync =
            new ThreadPoolExecutor(THREAD_SIZE,
                    THREAD_SIZE * 2,
                    60L,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(QUEUE_SIZE),
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setName("rabbitmq_client_async_sender");
                            return t;
                        }
                    },
                    new RejectedExecutionHandler() {
                        @Override
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                            log.error("async sender is error rejected, runnable:{}, executor:{}",
                                    r,
                                    executor);
                        }
                    }
            );

    public static void submit(Runnable r) {
        senderAsync.submit(r);
    }
}
