package com.sz.springbootsample.demo.controller;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sz.springbootsample.demo.service.CellTowerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Yanghj
 * @date 2024/4/16 17:48
 */
@RestController
@Validated
@RequestMapping("/cellTower")
@Api(tags = "蜂窝信号塔")
public class CellTowerController {

    @Resource private CellTowerService cellTowerService;

    @GetMapping("/count")
    @ApiOperation("按类型划分的基站数量")
    public Map<String, Long> countByType() {
        return cellTowerService.countByType();
    }
}
