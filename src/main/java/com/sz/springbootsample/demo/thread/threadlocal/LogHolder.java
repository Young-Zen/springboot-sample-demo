package com.sz.springbootsample.demo.thread.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.sz.springbootsample.demo.dto.LogDTO;

/**
 * @author Yanghj
 * @date 1/2/2020
 */
public class LogHolder {
    private static final TransmittableThreadLocal<LogDTO> LOGHOLDER = new TransmittableThreadLocal<>();

    public static void setLogDto(LogDTO logDto) {
        LOGHOLDER.set(logDto);
    }

    public static LogDTO getLogDto() {
        return LOGHOLDER.get();
    }

    public static void clean() {
        LOGHOLDER.remove();
    }
}
