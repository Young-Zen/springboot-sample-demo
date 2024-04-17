package com.sz.springbootsample.demo.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sz.springbootsample.demo.dao.mysql.DemoDAO;
import com.sz.springbootsample.demo.entity.mysql.DemoEntity;
import com.sz.springbootsample.demo.service.DemoService;
import com.sz.springbootsample.demo.thread.threadlocal.LogHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * Demo的相关服务的实现
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Service
@Slf4j
public class DemoServiceImpl extends ServiceImpl<DemoDAO, DemoEntity> implements DemoService {

    @Override
    @Async
    public void async() {
        log.info(LogHolder.getLogDto().getLogCode());
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName(System.currentTimeMillis() + "-test");
        demoEntity.setAge(10);
        super.save(demoEntity);
        super.getBaseMapper().updateAge(demoEntity.getPkDemoId());
    }
}
