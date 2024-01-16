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

    /**
     * demoVo to DemoPo
     *
     * @param demoVo
     * @return
     */
    @Mappings({@Mapping(source = "demoId", target = "pkDemoId")})
    DemoPO demoVo2DemoPo(DemoVO demoVo);

    /**
     * demoPo to DemoVO
     *
     * @param demoPo
     * @return
     */
    @Mappings({@Mapping(source = "pkDemoId", target = "demoId")})
    DemoVO demoPo2DemoVO(DemoPO demoPo);

    /**
     * demoVos to demoPos
     *
     * @param demoVos
     * @return
     */
    List<DemoPO> demoVos2demoPos(List<DemoVO> demoVos);

    /**
     * demoPos to demoVos
     *
     * @param demoPos
     * @return
     */
    List<DemoVO> demoPos2demoVos(List<DemoPO> demoPos);

    /**
     * demoPoPage to demoVoPage
     *
     * @param demoPoPage
     * @return
     */
    default IPage<DemoVO> demoPoPage2demoVoPage(IPage<DemoPO> demoPoPage) {
        if (demoPoPage == null) {
            return null;
        }
        IPage<DemoVO> demoVoPage =
                new Page<>(demoPoPage.getCurrent(), demoPoPage.getSize(), demoPoPage.getTotal());
        demoVoPage.setRecords(this.demoPos2demoVos(demoPoPage.getRecords()));
        return demoVoPage;
    }
}
