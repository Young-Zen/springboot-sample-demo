package com.sz.springbootsample.demo.service.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sz.springbootsample.demo.dao.UserDAO;
import com.sz.springbootsample.demo.entity.UserEntity;
import com.sz.springbootsample.demo.mapper.UserMapper;
import com.sz.springbootsample.demo.service.UserService;
import com.sz.springbootsample.demo.util.ParamValidateUtil;
import com.sz.springbootsample.demo.vo.UserVO;

/**
 * 用户服务实现类
 *
 * @author Yanghj
 * @since 2020-03-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDAO, UserEntity> implements UserService {

    @Resource private ParamValidateUtil paramValidateUtil;

    @CachePut(value = "USERS", key = "#result.userId", unless = "#result.userId == null")
    @Override
    public UserVO saveUser(UserVO userVo) {
        paramValidateUtil.verifyParams(userVo);

        UserEntity userEntity = UserMapper.INSTANCE.userVo2UserEntity(userVo);
        super.save(userEntity);

        return UserMapper.INSTANCE.userEntity2UserVO(userEntity);
    }

    @Cacheable(value = "USERS", key = "#userId", unless = "#result == null")
    @Override
    public UserVO getUser(Long userId) {
        UserEntity userEntity = super.getById(userId);
        return UserMapper.INSTANCE.userEntity2UserVO(userEntity);
    }

    @CachePut(value = "USERS", key = "#result.userId", unless = "#result.userId == null")
    @Override
    public UserVO updateUser(UserVO userVo) {
        UserEntity userEntity = UserMapper.INSTANCE.userVo2UserEntity(userVo);
        super.updateById(userEntity);

        return UserMapper.INSTANCE.userEntity2UserVO(userEntity);
    }

    @CacheEvict(value = "USERS", key = "#userId")
    @Override
    public void deleteUser(Long userId) {
        super.removeById(userId);
    }

    @Override
    public List<UserVO> listUsers() {
        List<UserEntity> userEntityList = super.list();
        return UserMapper.INSTANCE.userEntities2UserVos(userEntityList);
    }
}
