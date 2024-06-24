package com.sz.springbootsample.demo.service.grpc.server;

import com.sz.springbootsample.demo.config.grpc.GrpcService;

import io.grpc.example.stock.Stock;
import io.grpc.example.stock.StockQuote;
import io.grpc.example.stock.StockQuoteProviderGrpc;
import io.grpc.stub.StreamObserver;

/**
 * @author Yanghj
 * @date 2024/6/24 18:05
 */
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
}
