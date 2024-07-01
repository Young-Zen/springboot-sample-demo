package com.sz.springbootsample.demo.service.grpc.server;

import com.sz.springbootsample.demo.config.grpc.GrpcService;

import io.grpc.example.stock.Stock;
import io.grpc.example.stock.StockQuote;
import io.grpc.example.stock.StockQuoteProviderGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yanghj
 * @date 2024/6/24 18:05
 */
@Slf4j
@GrpcService
public class GrpcStockQuoteService extends StockQuoteProviderGrpc.StockQuoteProviderImplBase {

    @Override
    public void getStockQuote(Stock request, StreamObserver<StockQuote> responseObserver) {
        responseObserver.onNext(
                StockQuote.newBuilder()
                        .setPrice(request.getTickerSymbol().length())
                        .setOfferNumber(request.getCompanyName().length())
                        .setDescription(request.getDescription())
                        .build());
        responseObserver.onCompleted();
    }

    @Override
    public void serverSideStreamingGetListStockQuotes(
            Stock request, StreamObserver<StockQuote> responseObserver) {
        for (int i = 0; i <= request.getCompanyName().length(); i++) {
            StockQuote stockQuote =
                    StockQuote.newBuilder()
                            .setPrice(request.getTickerSymbol().length())
                            .setOfferNumber(i)
                            .setDescription("Price for stock:" + request.getTickerSymbol())
                            .build();
            responseObserver.onNext(stockQuote);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Stock> clientSideStreamingGetStatisticsOfStocks(
            StreamObserver<StockQuote> responseObserver) {
        return new StreamObserver<Stock>() {
            int count;
            double price = 0.0;
            StringBuffer sb = new StringBuffer();

            @Override
            public void onNext(Stock stock) {
                count++;
                price += stock.getTickerSymbol().length();
                sb.append(":").append(stock.getTickerSymbol());
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(
                        StockQuote.newBuilder()
                                .setPrice(price / count)
                                .setDescription("Statistics-" + sb.toString())
                                .build());
                responseObserver.onCompleted();
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("clientSideStreamingGetStatisticsOfStocks error: ", throwable);
            }
        };
    }

    @Override
    public StreamObserver<Stock> bidirectionalStreamingGetListsStockQuotes(
            StreamObserver<StockQuote> responseObserver) {
        return new StreamObserver<Stock>() {
            @Override
            public void onNext(Stock request) {
                StockQuote stockQuote =
                        StockQuote.newBuilder()
                                .setPrice(request.getTickerSymbol().length())
                                .setOfferNumber(request.getCompanyName().length())
                                .setDescription("Price for stock:" + request.getTickerSymbol())
                                .build();
                responseObserver.onNext(stockQuote);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("bidirectionalStreamingGetListsStockQuotes error: ", throwable);
            }
        };
    }
}
