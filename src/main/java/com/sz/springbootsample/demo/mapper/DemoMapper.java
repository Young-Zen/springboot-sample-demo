package com.sz.springbootsample.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.sz.springbootsample.demo.entity.mysql.DemoEntity;
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
     * demoVo to demoEntity
     *
     * @param demoVo
     * @return
     */
    @Mappings({@Mapping(source = "demoId", target = "pkDemoId")})
    DemoEntity demoVo2DemoEntity(DemoVO demoVo);

    /**
     * demoEntity to DemoVO
     *
     * @param demoEntity
     * @return
     */
    @Mappings({@Mapping(source = "pkDemoId", target = "demoId")})
    DemoVO demoEntity2DemoVO(DemoEntity demoEntity);

    /**
     * demoVos to demoEntities
     *
     * @param demoVos
     * @return
     */
    List<DemoEntity> demoVos2demoEntities(List<DemoVO> demoVos);

    /**
     * demoEntities to demoVos
     *
     * @param demoEntities
     * @return
     */
    List<DemoVO> demoEntities2demoVos(List<DemoEntity> demoEntities);

    /**
     * demoEntityPage to demoVoPage
     *
     * @param demoEntityPage
     * @return
     */
    default IPage<DemoVO> demoEntityPage2demoVoPage(IPage<DemoEntity> demoEntityPage) {
        if (demoEntityPage == null) {
            return null;
        }
        IPage<DemoVO> demoVoPage =
                new Page<>(
                        demoEntityPage.getCurrent(),
                        demoEntityPage.getSize(),
                        demoEntityPage.getTotal());
        demoVoPage.setRecords(this.demoEntities2demoVos(demoEntityPage.getRecords()));
        return demoVoPage;
    }
}
