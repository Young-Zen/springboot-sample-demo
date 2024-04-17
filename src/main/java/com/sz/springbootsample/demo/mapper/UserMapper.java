package com.sz.springbootsample.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.sz.springbootsample.demo.entity.mysql.UserEntity;
import com.sz.springbootsample.demo.vo.UserVO;

/**
 * Demo实体映射处理类
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * userVo to userEntity
     *
     * @param userVo
     * @return
     */
    @Mappings({@Mapping(source = "userId", target = "pkUserId")})
    UserEntity userVo2UserEntity(UserVO userVo);

    /**
     * userEntity to UserVO
     *
     * @param userEntity
     * @return
     */
    @Mappings({@Mapping(source = "pkUserId", target = "userId")})
    UserVO userEntity2UserVO(UserEntity userEntity);

    /**
     * userVos to userEntities
     *
     * @param userVos
     * @return
     */
    List<UserEntity> userVos2UserEntities(List<UserVO> userVos);

    /**
     * userEntities to UserVos
     *
     * @param userEntities
     * @return
     */
    List<UserVO> userEntities2UserVos(List<UserEntity> userEntities);

    /**
     * userEntityPage to UserVoPage
     *
     * @param userEntityPage
     * @return
     */
    default IPage<UserVO> userEntityPage2UserVoPage(IPage<UserEntity> userEntityPage) {
        if (userEntityPage == null) {
            return null;
        }
        IPage<UserVO> userVoPage =
                new Page<>(
                        userEntityPage.getCurrent(),
                        userEntityPage.getSize(),
                        userEntityPage.getTotal());
        userVoPage.setRecords(this.userEntities2UserVos(userEntityPage.getRecords()));
        return userVoPage;
    }
}
