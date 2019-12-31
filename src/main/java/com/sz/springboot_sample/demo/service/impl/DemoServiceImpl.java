package com.sz.springboot_sample.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sz.springboot_sample.demo.dao.DemoDAO;
import com.sz.springboot_sample.demo.po.DemoPO;
import com.sz.springboot_sample.demo.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoDAO, DemoPO> implements DemoService {
}
