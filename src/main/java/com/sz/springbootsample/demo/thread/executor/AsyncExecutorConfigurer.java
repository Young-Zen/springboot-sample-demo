package com.sz.springbootsample.demo.thread.executor;

import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Yanghj
 * @date 1/2/2020
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncExecutorConfigurer extends AsyncConfigurerSupport {

    @Value("${custom.async-executor.core-pool-size:4}")
    private int corePoolSize;

    @Value("${custom.async-executor.max-pool-size:8}")
    private int maxPoolSize;

    @Value("${custom.async-executor.queue-capacity:64}")
    private int queueCapacity;

    @Value("${custom.async-executor.keep-alive-seconds:60}")
    private int keepAliveSeconds;

    @Override
    public Executor getAsyncExecutor() {
        log.info("start asyncServiceExecutor");
        //使用 VisitableThreadPoolTaskExecutor
        ThreadPoolTaskExecutor executor = new VisitableThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        //空闲的多余线程最大存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return TtlExecutors.getTtlExecutor(executor);
    }
}
