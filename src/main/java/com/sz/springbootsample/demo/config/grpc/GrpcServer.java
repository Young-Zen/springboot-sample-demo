package com.sz.springbootsample.demo.config.grpc;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.sz.springbootsample.demo.config.property.GrpcProperties;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yanghj
 * @date 2024/6/24 17:31
 */
@Slf4j
@Component
public class GrpcServer implements CommandLineRunner, DisposableBean {

    @Resource private ApplicationContext applicationContext;
    @Resource private GrpcProperties grpcProperties;

    private Server server;

    @Override
    public void run(String... args) throws Exception {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(GrpcService.class);
        NettyServerBuilder serverBuilder =
                NettyServerBuilder.forPort(grpcProperties.getServer().getPort());
        for (String beanName : beanNames) {
            BindableService bindableService =
                    this.applicationContext.getBean(beanName, BindableService.class);
            serverBuilder.addService(bindableService);
            log.info("{} service registered", beanName);
        }

        if (grpcProperties.isEnableReflection()) {
            serverBuilder.addService(ProtoReflectionService.newInstance());
        }

        server = serverBuilder.build().start();
        log.info("GRPC server is start, port: {}", grpcProperties.getServer().getPort());
    }

    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(server)) {
            server.shutdown();
            log.info("shutdown GRPC server");
            try {
                server.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("shutdown interrupted", e);
            } finally {
                if (!server.isTerminated()) {
                    server.shutdownNow();
                }
            }
        }
    }
}
