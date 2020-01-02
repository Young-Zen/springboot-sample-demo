package com.sz.springboot_sample.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sz.springboot_sample.demo.annotation.IgnoreTracing;
import com.sz.springboot_sample.demo.dto.ResponseResultDTO;
import com.sz.springboot_sample.demo.exception.BaseException;
import com.sz.springboot_sample.demo.mapper.DemoMapper;
import com.sz.springboot_sample.demo.po.DemoPO;
import com.sz.springboot_sample.demo.service.DemoService;
import com.sz.springboot_sample.demo.vo.DemoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/demo/lombok/chain")
    public DemoVO chain() {
        DemoVO demoVO = new DemoVO();
        demoVO.setAge(1).setName("chain");
        return demoVO;
    }

    @GetMapping("/security")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseResultDTO security() {
        ResponseResultDTO responseResultDTO = null;
        responseResultDTO.getCode();
        return ResponseResultDTO.fail();
    }

    @GetMapping("/exception")
    public ResponseResultDTO exception() {
        throw new BaseException();
    }

    @GetMapping("/mybatisPlus")
    public ResponseResultDTO mybatisPlus() {
        DemoVO demoVO = new DemoVO();
        demoVO.setAge(1).setName("mybatisPlus").setAccount(5.20).setCreateTime(new Date());
        demoService.save(DemoMapper.INSTANCE.demoVO2DemoPO(demoVO));

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", "mybatisPlus");
        wrapper.last("limit 1");
        DemoPO demoPO = demoService.getOne(wrapper);
        return ResponseResultDTO.ok(DemoMapper.INSTANCE.demoPO2DemoVO(demoPO));
    }

    @GetMapping("/ignoreTracing")
    @IgnoreTracing
    public ResponseResultDTO ignoreTracing() {QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", "mybatisPlus");
        wrapper.last("limit 1");
        DemoPO demoPO = demoService.getOne(wrapper);
        return ResponseResultDTO.ok("look console");
    }

    @GetMapping("/async")
    public ResponseResultDTO async() {
        demoService.async();
        return ResponseResultDTO.ok("look console");
    }
}
