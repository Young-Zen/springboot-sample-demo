package com.sz.springbootsample.demo.service.grpc.client;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yanghj
 * @date 2024/6/24 16:59
 */
@Slf4j
public class BaseGrpcClient {

    private static final ConcurrentHashMap<String, ManagedChannel> MANAGED_CHANNEL_MAP =
            new ConcurrentHashMap<>();

    protected ManagedChannel openChannel(String host, int port) {
        return MANAGED_CHANNEL_MAP.computeIfAbsent(
                this.getClass().getSimpleName(),
                k -> {
                    ManagedChannel managedChannel =
                            ManagedChannelBuilder.forAddress(host, port)
                                    .enableRetry()
                                    .maxRetryAttempts(2)
                                    .usePlaintext()
                                    .build();
                    Runtime.getRuntime()
                            .addShutdownHook(
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            if (Objects.nonNull(managedChannel)) {
                                                managedChannel.shutdown();
                                                log.info("shutdown grpc client {}", k);
                                                try {
                                                    managedChannel.awaitTermination(
                                                            3, TimeUnit.SECONDS);
                                                } catch (InterruptedException e) {
                                                    log.error("shutdown interrupted", e);
                                                } finally {
                                                    if (!managedChannel.isTerminated()) {
                                                        managedChannel.shutdownNow();
                                                    }
                                                }
                                            }
                                        }
                                    });
                    return managedChannel;
                });
    }
}
