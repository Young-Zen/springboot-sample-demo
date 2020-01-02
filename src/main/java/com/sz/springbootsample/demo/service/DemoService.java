package com.sz.springbootsample.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sz.springbootsample.demo.po.DemoPO;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
public interface DemoService extends IService<DemoPO> {
    public void async();
}
