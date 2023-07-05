package com.sz.springbootsample.demo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sz.springbootsample.demo.po.UserPO;
import com.sz.springbootsample.demo.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Demo实体映射处理类
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(source = "userId",target = "pkUserId")
    })
    UserPO userVO2UserPO(UserVO userVO);

    @Mappings({
            @Mapping(source = "pkUserId",target = "userId")
    })
    UserVO userPO2UserVO(UserPO userPO);

    List<UserPO> userVOs2UserPOs(List<UserVO> userVOs);

    List<UserVO> userPOs2UserVOs(List<UserPO> userPOs);

    default IPage<UserVO> userPOPage2UserVOPage(IPage<UserPO> userPOIPage) {
        if (userPOIPage == null) {
            return null;
        }
        IPage<UserVO> userVOIPage = new Page<>(userPOIPage.getCurrent(), userPOIPage.getSize(), userPOIPage.getTotal());
        userVOIPage.setRecords(this.userPOs2UserVOs(userPOIPage.getRecords()));
        return userVOIPage;
    }
}
