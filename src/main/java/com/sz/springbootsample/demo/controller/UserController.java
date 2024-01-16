package com.sz.springbootsample.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sz.springbootsample.demo.dto.ResponseResultDTO;
import com.sz.springbootsample.demo.mapper.UserMapper;
import com.sz.springbootsample.demo.po.UserPO;
import com.sz.springbootsample.demo.service.UserService;
import com.sz.springbootsample.demo.util.ParamValidateUtil;
import com.sz.springbootsample.demo.vo.UserVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户前端控制器
 *
 * @author Yanghj
 * @since 2020-03-16
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户前端控制器")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private ParamValidateUtil paramValidateUtil;

    @PostMapping("/add")
    @ApiOperation("插入")
    public ResponseResultDTO add(
            @ApiParam(name = "UserVO对象", value = "json格式", required = true) @RequestBody
                    UserVO userVo) {
        paramValidateUtil.verifyParams(userVo);

        UserPO userPo = UserMapper.INSTANCE.userVo2UserPo(userVo);
        userService.save(userPo);

        // userPo = userService.getById(userPo.getPkUserId());
        return ResponseResultDTO.ok(UserMapper.INSTANCE.userPo2UserVO(userPo));
    }

    @PostMapping("/update")
    @ApiOperation("更新")
    public ResponseResultDTO update(
            @ApiParam(name = "UserVO对象", value = "json格式", required = true) @RequestBody
                    UserVO userVo) {
        UserPO userPo = UserMapper.INSTANCE.userVo2UserPo(userVo);
        userService.updateById(userPo);

        // userPo = userService.getById(userPo.getPkUserId());
        return ResponseResultDTO.ok(UserMapper.INSTANCE.userPo2UserVO(userPo));
    }

    @GetMapping("/list")
    @ApiOperation("用户列表")
    public ResponseResultDTO list() {
        List<UserPO> userPoList = userService.list();
        return ResponseResultDTO.ok(UserMapper.INSTANCE.userPos2UserVos(userPoList));
    }
}
