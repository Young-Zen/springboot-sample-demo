package com.sz.springbootsample.demo.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import com.sz.springbootsample.demo.entity.clickhouse.CellTowerEntity;

/**
 * @author Yanghj
 * @date 2024/4/16 17:42
 */
public interface CellTowerService extends IService<CellTowerEntity> {

    /**
     * 按类型划分的基站数量
     *
     * @return
     */
    Map<String, Long> countByType();
}
