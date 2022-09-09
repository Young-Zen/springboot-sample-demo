package com.sz.springbootsample.demo.util;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * this module only use for CD
 *
 * @author yanghaojia CD-Group
 * @date 2022/9/8 14:08
 */
public class RetryUtils {

    public static  <T> T retry(Supplier<T> fn, int maxRetries, long retryBackoffMillis, boolean exponential) {
        return retry(fn, maxRetries, Duration.ofMillis(retryBackoffMillis), exponential);
    }

    public static  <T> T retry(Supplier<T> fn, int maxRetries, Duration retryBackoff, boolean exponential) {
        int retries = 0;
        while (true) {
            try {
                return fn.get();
            } catch (Exception e) {
                if (retries >= (maxRetries - 1)) {
                    throw e;
                }

                long timeout =
                        !exponential
                                ? retryBackoff.toMillis()
                                : (long) Math.pow(2, retries) * retryBackoff.toMillis();
                sleep(timeout);

                retries++;
            }
        }
    }

    /** Overridable by test cases to avoid Thread.sleep() */
    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
