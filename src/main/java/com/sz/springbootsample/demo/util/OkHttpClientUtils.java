package com.sz.springbootsample.demo.util;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * this module only use for CD
 *
 * @author yanghaojia CD-Group
 * @date 2021/3/25 14:28
 */
public final class OkHttpClientUtils {

    private static final int DEFAULT_RETRY_COUNT = 3;
    private static final int DEFAULT_RETRY_INTERVAL = 1000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 6000;
    private static final int DEFAULT_READ_TIMEOUT = 30000;
    private static final int DEFAULT_WRITE_TIMEOUT = 30000;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClientUtils() {
    }

    public static OkHttpClient createHttpClient() {
        return getOkHttpClientBuilder().build();
    }

    public static OkHttpClient createHttpClient(String token) {
        return getOkHttpClientBuilder().addInterceptor(new TokenAuthInterceptor(token)).build();
    }

    public static OkHttpClient createHttpClient(String user, String password) {
        return getOkHttpClientBuilder().addInterceptor(new BasicAuthInterceptor(user, password)).build();
    }

    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(new RetryInterceptor.Builder()
                        .count(DEFAULT_RETRY_COUNT)
                        .interval(DEFAULT_RETRY_INTERVAL).build())
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public static String get(OkHttpClient okHttpClient, String url) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        builder = builder.get();
        Response response = okHttpClient.newCall(builder.build()).execute();
        if (!response.isSuccessful()) {
            String responseBody = "";
            if (response.body() != null) {
                responseBody = response.body().string();
            }
            throw new IOException(String.format("Fail to request GET %s: response %d %s", url, response.code(), responseBody));
        }
        return response.body().string();
    }

    public static String postJson(OkHttpClient okHttpClient, String url, String json) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        builder = builder.post(RequestBody.create(JSON, json));
        Response response = okHttpClient.newCall(builder.build()).execute();
        if (!response.isSuccessful()) {
            String responseBody = "";
            if (response.body() != null) {
                responseBody = response.body().string();
            }
            throw new IOException(String.format("Fail to request POST %s: response %d %s", url, response.code(), responseBody));
        }
        return response.body().string();
    }

    private static class TokenAuthInterceptor implements Interceptor {
        private String credentials;

        public TokenAuthInterceptor(final String token) {
            credentials = "token " + token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder().header("Authorization", credentials).build();
            return chain.proceed(authenticatedRequest);
        }
    }

    private static class BasicAuthInterceptor implements Interceptor {
        private String credentials;

        public BasicAuthInterceptor(final String user, final String password) {
            credentials = Credentials.basic(user, password);
        }

        @Override
        public Response intercept(final Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder().header("Authorization", credentials).build();
            return chain.proceed(authenticatedRequest);
        }
    }

    private static class RetryInterceptor implements Interceptor {
        private int count;
        private long interval;

        RetryInterceptor(Builder builder) {
            this.count = builder.count;
            this.interval = builder.interval;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            return doRequest(chain, request);
        }

        private Response doRequest(Chain chain, Request request) throws IOException {
            try {
                Response response = chain.proceed(request);
                int statusCode = response.code();
                if (statusCode >= 200 && statusCode < 500) {
                    return response;
                }
                if (count > 0) {
                    return retry(chain, request);
                }
                throw new IOException("response not successful and run out of retries");
            } catch (IOException e) {
                if (count > 0) {
                    return retry(chain, request);
                }
                throw e;
            }
        }

        private Response retry(Chain chain, Request request) throws IOException {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            count--;
            return doRequest(chain, request);
        }

        public static final class Builder {
            private int count;
            private int interval;

            public Builder() {
                count = DEFAULT_RETRY_COUNT;
                interval = DEFAULT_RETRY_INTERVAL;
            }

            public Builder count(int count) {
                this.count = count;
                return this;
            }

            public Builder interval(int interval) {
                this.interval = interval;
                return this;
            }

            public RetryInterceptor build() {
                return new RetryInterceptor(this);
            }
        }
    }
}
