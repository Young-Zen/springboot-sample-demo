package com.sz.springbootsample.demo.service.impl;

import com.sz.springbootsample.demo.dao.UserDAO;
import com.sz.springbootsample.demo.po.UserPO;
import com.sz.springbootsample.demo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author Yanghj
 * @since 2020-03-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDAO, UserPO> implements UserService {

    @Autowired
    private UserDAO userDAO;
}
