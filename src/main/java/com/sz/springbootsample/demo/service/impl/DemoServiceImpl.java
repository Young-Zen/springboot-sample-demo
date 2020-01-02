package com.sz.springbootsample.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sz.springbootsample.demo.dao.DemoDAO;
import com.sz.springbootsample.demo.po.DemoPO;
import com.sz.springbootsample.demo.service.DemoService;
import com.sz.springbootsample.demo.thread.threadlocal.LogHolder;
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
