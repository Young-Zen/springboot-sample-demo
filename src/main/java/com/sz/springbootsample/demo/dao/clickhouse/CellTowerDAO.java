package com.sz.springbootsample.demo.dao.clickhouse;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.sz.springbootsample.demo.entity.clickhouse.CellTowerEntity;

/**
 * @author Yanghj
 * @date 2024/4/16 17:34
 */
@Mapper
public interface CellTowerDAO extends BaseMapper<CellTowerEntity> {

    /**
     * 按类型划分的基站数量
     *
     * @return
     */
    List<Map<String, Object>> countByType();
}
