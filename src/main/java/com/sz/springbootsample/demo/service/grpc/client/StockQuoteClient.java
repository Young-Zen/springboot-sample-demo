package com.sz.springbootsample.demo.service.grpc.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.sz.springbootsample.demo.config.property.GrpcProperties;

import io.grpc.ManagedChannel;
import io.grpc.example.stock.Stock;
import io.grpc.example.stock.StockQuote;
import io.grpc.example.stock.StockQuoteProviderGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yanghj
 * @date 2024/6/24 16:40
 */
@Slf4j
@Component
public class StockQuoteClient extends BaseGrpcClient {

    private StockQuoteProviderGrpc.StockQuoteProviderBlockingStub blockingStub;
    private StockQuoteProviderGrpc.StockQuoteProviderStub stub;

    public StockQuoteClient(GrpcProperties grpcProperties) {
        ManagedChannel managedChannel =
                super.openChannel(
                        grpcProperties.getClient().getStockQuote().getHost(),
                        grpcProperties.getClient().getStockQuote().getPort());
        blockingStub = StockQuoteProviderGrpc.newBlockingStub(managedChannel);
        stub = StockQuoteProviderGrpc.newStub(managedChannel);
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

    public List<StockQuote> listStockQuotes(
            String tickerSymbol, String companyName, String description) {
        Stock stock =
                Stock.newBuilder()
                        .setTickerSymbol(Optional.ofNullable(tickerSymbol).orElse(""))
                        .setCompanyName(Optional.ofNullable(companyName).orElse(""))
                        .setDescription(Optional.ofNullable(description).orElse(""))
                        .build();
        Iterator<StockQuote> stockQuoteIterator =
                blockingStub.serverSideStreamingGetListStockQuotes(stock);
        List<StockQuote> stockQuoteList = new ArrayList<>();
        while (stockQuoteIterator.hasNext()) {
            stockQuoteList.add(stockQuoteIterator.next());
        }
        return stockQuoteList;
    }

    public StockQuote getStatisticsOfStocks(List<Stock> stocks) {
        CompletableFuture<StockQuote> future = new CompletableFuture<>();
        StreamObserver<StockQuote> responseObserver =
                new StreamObserver<StockQuote>() {
                    @Override
                    public void onNext(StockQuote stockQuote) {
                        future.complete(stockQuote);
                    }

                    @Override
                    public void onCompleted() {
                        log.info("Finished clientSideStreamingGetStatisticsOfStocks");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        log.error("getStatisticsOfStocks error: ", throwable);
                    }
                };
        StreamObserver<Stock> requestObserver =
                stub.clientSideStreamingGetStatisticsOfStocks(responseObserver);
        for (Stock stock : stocks) {
            requestObserver.onNext(stock);
        }
        requestObserver.onCompleted();

        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<StockQuote> bidirectionalStreamingGetListsStockQuotes(List<Stock> stocks) {
        CompletableFuture<List<StockQuote>> future = new CompletableFuture<>();
        StreamObserver<StockQuote> responseObserver =
                new StreamObserver<StockQuote>() {
                    List<StockQuote> stockQuoteList = new ArrayList<>();

                    @Override
                    public void onNext(StockQuote stockQuote) {
                        stockQuoteList.add(stockQuote);
                    }

                    @Override
                    public void onCompleted() {
                        future.complete(stockQuoteList);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        log.error("bidirectionalStreamingGetListsStockQuotes error: ", throwable);
                    }
                };

        StreamObserver<Stock> requestObserver =
                stub.bidirectionalStreamingGetListsStockQuotes(responseObserver);
        for (Stock stock : stocks) {
            requestObserver.onNext(stock);
        }
        requestObserver.onCompleted();

        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
