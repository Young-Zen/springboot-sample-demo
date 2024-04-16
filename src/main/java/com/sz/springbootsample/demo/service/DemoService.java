package com.sz.springbootsample.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.sz.springbootsample.demo.entity.DemoEntity;

/**
 * Demo相关的服务接口
 *
 * @author Yanghj
 * @date 1/1/2020
 */
public interface DemoService extends IService<DemoEntity> {
    /** test async */
    public void async();
}
