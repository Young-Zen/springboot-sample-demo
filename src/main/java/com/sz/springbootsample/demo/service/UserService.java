package com.sz.springbootsample.demo.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import com.sz.springbootsample.demo.entity.mysql.UserEntity;
import com.sz.springbootsample.demo.vo.UserVO;

/**
 * 用户服务类
 *
 * @author Yanghj
 * @since 2020-03-16
 */
public interface UserService extends IService<UserEntity> {

    /**
     * save User
     *
     * @param userVo
     * @return
     */
    UserVO saveUser(UserVO userVo);

    /**
     * get User
     *
     * @param userId
     * @return
     */
    UserVO getUser(Long userId);

    /**
     * update User
     *
     * @param userVo
     * @return
     */
    UserVO updateUser(UserVO userVo);

    /**
     * delete User
     *
     * @param userId
     */
    void deleteUser(Long userId);

    /**
     * list Users
     *
     * @return
     */
    List<UserVO> listUsers();
}
