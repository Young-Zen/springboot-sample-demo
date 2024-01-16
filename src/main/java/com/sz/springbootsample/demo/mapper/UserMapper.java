package com.sz.springbootsample.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.sz.springbootsample.demo.po.UserPO;
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
     * userVo to UserPo
     *
     * @param userVo
     * @return
     */
    @Mappings({@Mapping(source = "userId", target = "pkUserId")})
    UserPO userVo2UserPo(UserVO userVo);

    /**
     * userPo to UserVO
     *
     * @param userPo
     * @return
     */
    @Mappings({@Mapping(source = "pkUserId", target = "userId")})
    UserVO userPo2UserVO(UserPO userPo);

    /**
     * userVos to UserPos
     *
     * @param userVos
     * @return
     */
    List<UserPO> userVos2UserPos(List<UserVO> userVos);

    /**
     * userPos to UserVos
     *
     * @param userPos
     * @return
     */
    List<UserVO> userPos2UserVos(List<UserPO> userPos);

    /**
     * userPoPage to UserVoPage
     *
     * @param userPoPage
     * @return
     */
    default IPage<UserVO> userPoPage2UserVoPage(IPage<UserPO> userPoPage) {
        if (userPoPage == null) {
            return null;
        }
        IPage<UserVO> userVoPage =
                new Page<>(userPoPage.getCurrent(), userPoPage.getSize(), userPoPage.getTotal());
        userVoPage.setRecords(this.userPos2UserVos(userPoPage.getRecords()));
        return userVoPage;
    }
}
