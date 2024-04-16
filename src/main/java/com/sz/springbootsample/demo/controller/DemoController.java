package com.sz.springbootsample.demo.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.sz.springbootsample.demo.annotation.IgnoreTracing;
import com.sz.springbootsample.demo.config.message.RabbitConfig;
import com.sz.springbootsample.demo.dto.ResponseResultDTO;
import com.sz.springbootsample.demo.entity.DemoEntity;
import com.sz.springbootsample.demo.exception.BaseException;
import com.sz.springbootsample.demo.mapper.DemoMapper;
import com.sz.springbootsample.demo.service.DemoService;
import com.sz.springbootsample.demo.util.RedisUtils;
import com.sz.springbootsample.demo.vo.DemoVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Demo演示类
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@RestController
@Validated
@RequestMapping("/demo")
@Api(tags = "Demo 演示类")
public class DemoController {

    @Autowired private DemoService demoService;

    @Resource private RedisTemplate<String, Object> redisTemplate;

    @Autowired private RabbitTemplate rabbitTemplate;

    @GetMapping("/lombok/chain")
    @ApiOperation("Lombok 链式 set 方法例子")
    public DemoVO chain() {
        DemoVO demoVo = new DemoVO();
        demoVo.setDemoId(520L).setName("chain").setAge(1).setCreateTime(new Date());
        DemoEntity demoEntity = DemoMapper.INSTANCE.demoVo2DemoEntity(demoVo);
        return DemoMapper.INSTANCE.demoEntity2DemoVO(demoEntity);
    }

    @GetMapping("/security")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiOperation("Security 方法上 @PreAuthorize 例子")
    public ResponseResultDTO security() {
        ResponseResultDTO responseResultDTO = null;
        responseResultDTO.getCode();
        return ResponseResultDTO.fail();
    }

    @GetMapping("/exception")
    @ApiOperation("抛出 BaseException 例子")
    public ResponseResultDTO exception() {
        throw new BaseException();
    }

    @PostMapping("/add")
    @ApiOperation("插入")
    public ResponseResultDTO add(
            @ApiParam(name = "DemoVO对象", value = "json格式", required = true)
                    @Validated(DemoVO.Add.class)
                    @RequestBody
                    DemoVO demoVo) {
        demoService.save(DemoMapper.INSTANCE.demoVo2DemoEntity(demoVo));

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.last("limit 1");
        wrapper.orderByDesc("pk_demo_id");
        DemoEntity demoEntity = demoService.getOne(wrapper);
        return ResponseResultDTO.ok(DemoMapper.INSTANCE.demoEntity2DemoVO(demoEntity));
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseResultDTO delete(
            @ApiParam(value = "正整数", required = true)
                    @NotNull(message = "ID不能为空")
                    @Min(value = 1, message = "ID最小为1")
                    @RequestParam("id")
                    Long id) {
        demoService.removeById(id);
        return ResponseResultDTO.ok();
    }

    @GetMapping("/mybatisPlus")
    @ApiOperation("MybatisPlus 例子")
    public ResponseResultDTO mybatisPlus() {
        DemoVO demoVo = new DemoVO();
        demoVo.setAge(1)
                .setName("mybatisPlus")
                .setAccount(new BigDecimal("5.2"))
                .setCreateTime(new Date());
        demoService.save(DemoMapper.INSTANCE.demoVo2DemoEntity(demoVo));

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", "mybatisPlus");
        wrapper.last("limit 1");
        wrapper.orderByDesc("pk_demo_id");
        DemoEntity demoEntity = demoService.getOne(wrapper);
        return ResponseResultDTO.ok(DemoMapper.INSTANCE.demoEntity2DemoVO(demoEntity));
    }

    @GetMapping("/mybatisPlus/list")
    @ApiOperation("MybatisPlus list 方法例子")
    public ResponseResultDTO list() {
        List<DemoEntity> demoEntityList = demoService.list();
        return ResponseResultDTO.ok(DemoMapper.INSTANCE.demoEntities2demoVos(demoEntityList));
    }

    @GetMapping("/mybatisPlus/page")
    @ApiOperation("MybatisPlus 分页查询例子")
    public ResponseResultDTO page() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.orderByDesc("pk_demo_id");
        IPage<DemoEntity> page = demoService.page(new Page<>(0, 10), wrapper);
        return ResponseResultDTO.ok(DemoMapper.INSTANCE.demoEntityPage2demoVoPage(page));
    }

    @GetMapping("/ignoreTracing")
    @IgnoreTracing
    @ApiOperation("@IgnoreTracing 例子")
    public ResponseResultDTO ignoreTracing() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", "mybatisPlus");
        wrapper.last("limit 1");
        DemoEntity demoEntity = demoService.getOne(wrapper);
        return ResponseResultDTO.ok("look console");
    }

    @GetMapping("/async")
    @ApiOperation("异步方法例子")
    public ResponseResultDTO async() {
        demoService.async();
        return ResponseResultDTO.ok("look console");
    }

    @GetMapping("/redis")
    public void redis() {
        DemoVO demoVo = new DemoVO();
        demoVo.setName("name").setAge(10).setAccount(new BigDecimal("5.2"));
        redisTemplate.opsForValue().set("demoVo-1", demoVo, 300, TimeUnit.SECONDS);
    }

    @GetMapping("/redis/id")
    public long redisId() {
        return RedisUtils.getInstance().generateId("key1");
    }

    @GetMapping("/redis/distributedLock")
    public void distributedLock() {
        String key = "lock-1";
        long seconds = 100L;
        String clientId = UUID.randomUUID().toString();
        try {
            if (RedisUtils.getInstance().tryLock(key, clientId, seconds)) {
                System.out.println("lock");
            }
        } finally {
            RedisUtils.getInstance().unLock(key, clientId);
        }
    }

    @GetMapping("/rabbitmq/direct")
    public void direct() {
        DemoVO demoVo = new DemoVO();
        demoVo.setName("direct").setAge(10).setAccount(new BigDecimal("5.2"));
        rabbitTemplate.convertAndSend(
                RabbitConfig.MAIL_DIRECT_EXCHANGE, RabbitConfig.MAIL_ROUTING_KEY, demoVo);
    }

    @GetMapping("/rabbitmq/fanout")
    public void fanout() {
        DemoVO demoVo = new DemoVO();
        demoVo.setName("fanout").setAge(10).setAccount(new BigDecimal("5.2"));
        rabbitTemplate.convertAndSend(RabbitConfig.MAIL_FANOUT_EXCHANGE, "", demoVo);
    }
}
