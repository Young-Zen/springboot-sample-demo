package com.sz.springbootsample.demo.service.grpc.client;

import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sz.springbootsample.demo.config.property.GrpcProperties;

import io.grpc.ManagedChannel;
import io.grpc.example.stock.Stock;
import io.grpc.example.stock.StockQuote;
import io.grpc.example.stock.StockQuoteProviderGrpc;

/**
 * @author Yanghj
 * @date 2024/6/24 16:40
 */
@Component
public class StockQuoteClient extends BaseGrpcClient {

    @Resource private GrpcProperties grpcProperties;

    private StockQuoteProviderGrpc.StockQuoteProviderBlockingStub blockingStub;

    @PostConstruct
    void init() {
        ManagedChannel managedChannel =
                super.openChannel(
                        grpcProperties.getClient().getStockQuote().getHost(),
                        grpcProperties.getClient().getStockQuote().getPort());
        blockingStub = StockQuoteProviderGrpc.newBlockingStub(managedChannel);
    }

    public StockQuote getStockQuote(String tickerSymbol, String companyName, String description) {
        Stock stock =
                Stock.newBuilder()
                        .setTickerSymbol(Optional.ofNullable(tickerSymbol).orElse(""))
                        .setCompanyName(Optional.ofNullable(companyName).orElse(""))
                        .setDescription(Optional.ofNullable(description).orElse(""))
                        .build();
        return blockingStub.getStockQuote(stock);
    }
}
