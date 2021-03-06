package com.sz.springbootsample.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sz.springbootsample.demo.annotation.IgnoreTracing;
import com.sz.springbootsample.demo.config.message.RabbitConfig;
import com.sz.springbootsample.demo.dto.ResponseResultDTO;
import com.sz.springbootsample.demo.exception.BaseException;
import com.sz.springbootsample.demo.mapper.DemoMapper;
import com.sz.springbootsample.demo.po.DemoPO;
import com.sz.springbootsample.demo.service.DemoService;
import com.sz.springbootsample.demo.util.RedisUtils;
import com.sz.springbootsample.demo.vo.DemoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Demo演示类
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@RestController
@Validated  //校验方法参数
@Api(tags = "Demo 演示类")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/demo/lombok/chain")
    @ApiOperation("Lombok 链式 set 方法例子")
    public DemoVO chain() {
        DemoVO demoVO = new DemoVO();
        demoVO.setDemoId(520L).setName("chain").setAge(1).setCreateTime(new Date());
        DemoPO demoPO = DemoMapper.INSTANCE.demoVO2DemoPO(demoVO);
        return DemoMapper.INSTANCE.demoPO2DemoVO(demoPO);
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
    public ResponseResultDTO add(@ApiParam(name = "DemoVO对象", value = "json格式", required = true) @Validated(DemoVO.Add.class) @RequestBody DemoVO demoVO) {
        demoService.save(DemoMapper.INSTANCE.demoVO2DemoPO(demoVO));

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.last("limit 1");
        wrapper.orderByDesc("pk_demo_id");
        DemoPO demoPO = demoService.getOne(wrapper);
        return ResponseResultDTO.ok(DemoMapper.INSTANCE.demoPO2DemoVO(demoPO));
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseResultDTO delete(@ApiParam(value = "正整数", required = true) @NotNull(message = "ID不能为空") @Min(value = 1, message = "ID最小为1") @RequestParam("id") Long id) {
        demoService.removeById(id);
        return ResponseResultDTO.ok();
    }

    @GetMapping("/mybatisPlus")
    @ApiOperation("MybatisPlus 例子")
    public ResponseResultDTO mybatisPlus() {
        DemoVO demoVO = new DemoVO();
        demoVO.setAge(1).setName("mybatisPlus").setAccount(new BigDecimal("5.2")).setCreateTime(new Date());
        demoService.save(DemoMapper.INSTANCE.demoVO2DemoPO(demoVO));

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", "mybatisPlus");
        wrapper.last("limit 1");
        wrapper.orderByDesc("pk_demo_id");
        DemoPO demoPO = demoService.getOne(wrapper);
        return ResponseResultDTO.ok(DemoMapper.INSTANCE.demoPO2DemoVO(demoPO));
    }

    @GetMapping("/mybatisPlus/list")
    @ApiOperation("MybatisPlus list 方法例子")
    public ResponseResultDTO list() {
        List<DemoPO> demoPOList = demoService.list();
        return ResponseResultDTO.ok(DemoMapper.INSTANCE.demoPOs2demoVOs(demoPOList));
    }

    @GetMapping("/mybatisPlus/page")
    @ApiOperation("MybatisPlus 分页查询例子")
    public ResponseResultDTO page() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.orderByDesc("pk_demo_id");
        IPage<DemoPO> page = demoService.page(new Page<>(0, 10), wrapper);
        return ResponseResultDTO.ok(DemoMapper.INSTANCE.demoPOPage2demoVOPage(page));
    }

    @GetMapping("/ignoreTracing")
    @IgnoreTracing
    @ApiOperation("@IgnoreTracing 例子")
    public ResponseResultDTO ignoreTracing() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", "mybatisPlus");
        wrapper.last("limit 1");
        DemoPO demoPO = demoService.getOne(wrapper);
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
        DemoVO demoVO = new DemoVO();
        demoVO.setName("name").setAge(10).setAccount(new BigDecimal("5.2"));
        redisTemplate.opsForValue().set("demoVO-1", demoVO, 300, TimeUnit.SECONDS);
    }

    @GetMapping("/redis/id")
    public long redisID() {
        return RedisUtils.getInstance().generateID("key1");
    }

    @GetMapping("/redis/distributedLock")
    public void distributedLock() {
        String key = "lock-1";
        String clientId = UUID.randomUUID().toString();
        try {
            if (RedisUtils.getInstance().tryLock(key, clientId, 100)) {
                System.out.println("lock");
            }
        } finally {
            RedisUtils.getInstance().unLock(key, clientId);
        }
    }

    @GetMapping("/rabbitmq/direct")
    public void direct() {
        DemoVO demoVO = new DemoVO();
        demoVO.setName("direct").setAge(10).setAccount(new BigDecimal("5.2"));
        rabbitTemplate.convertAndSend(RabbitConfig.MAIL_DIRECT_EXCHANGE, RabbitConfig.MAIL_ROUTING_KEY, demoVO);
    }

    @GetMapping("/rabbitmq/fanout")
    public void fanout() {
        DemoVO demoVO = new DemoVO();
        demoVO.setName("fanout").setAge(10).setAccount(new BigDecimal("5.2"));
        rabbitTemplate.convertAndSend(RabbitConfig.MAIL_FANOUT_EXCHANGE, "", demoVO);
    }
}
