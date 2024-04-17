package com.sz.springbootsample.demo.dao.mysql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.sz.springbootsample.demo.entity.mysql.DemoEntity;

/**
 * Demo数据访问对象
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Mapper
public interface DemoDAO extends BaseMapper<DemoEntity> {

    /**
     * update age
     *
     * @param id
     */
    void updateAge(@Param("id") Long id);
}
