package com.sz.springbootsample.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sz.springbootsample.demo.dto.ResponseResultDTO;
import com.sz.springbootsample.demo.mapper.UserMapper;
import com.sz.springbootsample.demo.po.UserPO;
import com.sz.springbootsample.demo.service.UserService;
import com.sz.springbootsample.demo.util.ParamValidateUtil;
import com.sz.springbootsample.demo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户前端控制器
 * </p>
 *
 * @author Yanghj
 * @since 2020-03-16
 */
@RestController
@RequestMapping("/demo/user")
@Api(tags = "用户前端控制器")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ParamValidateUtil paramValidateUtil;

    @PostMapping("/add")
    @ApiOperation("插入")
    public ResponseResultDTO add(@ApiParam(name = "UserVO对象", value = "json格式", required = true) @RequestBody UserVO userVO) {
        paramValidateUtil.verifyParams(userVO);

        UserPO userPO = UserMapper.INSTANCE.userVO2UserPO(userVO);
        userService.save(userPO);

        //userPO = userService.getById(userPO.getPkUserId());
        return ResponseResultDTO.ok(UserMapper.INSTANCE.userPO2UserVO(userPO));
    }

    @PostMapping("/update")
    @ApiOperation("更新")
    public ResponseResultDTO update(@ApiParam(name = "UserVO对象", value = "json格式", required = true) @RequestBody UserVO userVO) {
        UserPO userPO = UserMapper.INSTANCE.userVO2UserPO(userVO);
        userService.updateById(userPO);

        //userPO = userService.getById(userPO.getPkUserId());
        return ResponseResultDTO.ok(UserMapper.INSTANCE.userPO2UserVO(userPO));
    }

    @GetMapping("/list")
    @ApiOperation("用户列表")
    public ResponseResultDTO list() {
        List<UserPO> userPOList = userService.list();
        return ResponseResultDTO.ok(UserMapper.INSTANCE.userPOs2UserVOs(userPOList));
    }
}
