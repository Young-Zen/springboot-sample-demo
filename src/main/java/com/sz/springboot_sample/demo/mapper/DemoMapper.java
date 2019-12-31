package com.sz.springboot_sample.demo.mapper;

import com.sz.springboot_sample.demo.po.DemoPO;
import com.sz.springboot_sample.demo.vo.DemoVO;
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
