package com.sz.springboot_sample.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sz.springboot_sample.demo.dao.DemoDAO;
import com.sz.springboot_sample.demo.po.DemoPO;
import com.sz.springboot_sample.demo.service.DemoService;
import com.sz.springboot_sample.demo.thread.threadlocal.LogHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@Service
@Slf4j
public class DemoServiceImpl extends ServiceImpl<DemoDAO, DemoPO> implements DemoService {

    @Override
    @Async
    public void async() {
        log.info(LogHolder.getLogDto().getLogCode());
    }
}
