package com.sz.springbootsample.demo.mapper;

import com.sz.springbootsample.demo.po.DemoPO;
import com.sz.springbootsample.demo.vo.DemoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@Mapper
public interface DemoMapper {
    DemoMapper INSTANCE= Mappers.getMapper(DemoMapper.class);

    DemoPO demoVO2DemoPO(DemoVO demoVO);

    DemoVO demoPO2DemoVO(DemoPO demoPO);
}
