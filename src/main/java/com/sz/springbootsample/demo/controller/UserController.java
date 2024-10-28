package com.sz.springbootsample.demo.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.sz.springbootsample.demo.dto.ResponseResultDTO;
import com.sz.springbootsample.demo.service.UserService;
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

    @Resource private UserService userService;

    @PostMapping("/add")
    @ApiOperation("插入")
    public ResponseResultDTO add(
            @ApiParam(name = "UserVO对象", value = "json格式", required = true) @Valid @RequestBody
                    UserVO userVo) {
        return ResponseResultDTO.ok(userService.saveUser(userVo));
    }

    @PostMapping("/{userId}")
    @ApiOperation("获取")
    public ResponseResultDTO get(@PathVariable("userId") Long userId) {
        return ResponseResultDTO.ok(userService.getUser(userId));
    }

    @PostMapping("/update")
    @ApiOperation("更新")
    public ResponseResultDTO update(
            @ApiParam(name = "UserVO对象", value = "json格式", required = true) @Valid @RequestBody
                    UserVO userVo) {
        return ResponseResultDTO.ok(userService.updateUser(userVo));
    }

    @DeleteMapping("/{userId}")
    @ApiOperation("获取")
    public ResponseResultDTO delete(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseResultDTO.ok();
    }

    @GetMapping("/list")
    @ApiOperation("用户列表")
    public ResponseResultDTO list() {
        return ResponseResultDTO.ok(userService.listUsers());
    }
}
