package com.sz.springbootsample.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sz.springbootsample.demo.po.DemoPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Demo数据访问对象
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Mapper
public interface DemoDAO extends BaseMapper<DemoPO> {
}
