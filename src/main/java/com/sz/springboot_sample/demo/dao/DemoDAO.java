package com.sz.springboot_sample.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sz.springboot_sample.demo.po.DemoPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@Mapper
public interface DemoDAO extends BaseMapper<DemoPO> {
}
