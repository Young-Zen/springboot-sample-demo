package com.sz.springboot_sample.demo.controller;

import com.sz.springboot_sample.demo.dto.ResponseResultDTO;
import com.sz.springboot_sample.demo.vo.DemoVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@RestController
public class DemoController {

    @GetMapping("/demo/lombok/chain")
    public DemoVO chain() {
        DemoVO demoVO = new DemoVO();
        demoVO.setAge(1).setName("chain");
        return demoVO;
    }

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseResultDTO test() {
        ResponseResultDTO responseResultDTO = null;
        responseResultDTO.getCode();
        return ResponseResultDTO.fail();
    }
}
