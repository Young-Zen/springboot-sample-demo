package com.sz.springbootsample.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.sz.springbootsample.demo.po.DemoPO;
import com.sz.springbootsample.demo.vo.DemoVO;

/**
 * Demo实体映射处理类 http://www.kailing.pub/MapStruct1.3/index.html
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Mapper
public interface DemoMapper {
    DemoMapper INSTANCE = Mappers.getMapper(DemoMapper.class);

    @Mappings({@Mapping(source = "demoId", target = "pkDemoId")})
    DemoPO demoVO2DemoPO(DemoVO demoVO);

    @Mappings({@Mapping(source = "pkDemoId", target = "demoId")})
    DemoVO demoPO2DemoVO(DemoPO demoPO);

    List<DemoPO> demoVOs2demoPOs(List<DemoVO> demoVOs);

    List<DemoVO> demoPOs2demoVOs(List<DemoPO> demoPOs);

    default IPage<DemoVO> demoPOPage2demoVOPage(IPage<DemoPO> demoPOIPage) {
        if (demoPOIPage == null) {
            return null;
        }
        IPage<DemoVO> demoVOIPage =
                new Page<>(demoPOIPage.getCurrent(), demoPOIPage.getSize(), demoPOIPage.getTotal());
        demoVOIPage.setRecords(this.demoPOs2demoVOs(demoPOIPage.getRecords()));
        return demoVOIPage;
    }
}
