package com.sz.springbootsample.demo.service.impl;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sz.springbootsample.demo.dao.clickhouse.CellTowerDAO;
import com.sz.springbootsample.demo.entity.clickhouse.CellTowerEntity;
import com.sz.springbootsample.demo.service.CellTowerService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Yanghj
 * @date 2024/4/16 17:43
 */
@Slf4j
@Service
public class CellTowerServiceImpl extends ServiceImpl<CellTowerDAO, CellTowerEntity>
        implements CellTowerService {

    @Override
    public Map<String, Long> countByType() {
        QueryWrapper<CellTowerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit 1");
        CellTowerEntity cellTowerEntity = super.getBaseMapper().selectOne(queryWrapper);
        log.info(cellTowerEntity.toString());
        return super.getBaseMapper().countByType().stream()
                .collect(
                        Collectors.toMap(
                                m -> m.get("radio").toString(),
                                m -> Long.parseLong(m.get("total").toString())));
    }
}
