package com.sz.springbootsample.demo.thread.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.sz.springbootsample.demo.dto.LogDTO;

/**
 * @author Yanghj
 * @date 1/2/2020
 */
public class LogHolder {
    private static final TransmittableThreadLocal<LogDTO> logHolder = new TransmittableThreadLocal<>();

    public static void setLogDto(LogDTO logDto) {
        logHolder.set(logDto);
    }

    public static LogDTO getLogDto() {
        return logHolder.get();
    }

    public static void clean() {
        logHolder.remove();
    }
}
