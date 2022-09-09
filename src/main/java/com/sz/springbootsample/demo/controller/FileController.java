package com.sz.springbootsample.demo.controller;

import com.sz.springbootsample.demo.annotation.IgnoreTracing;
import com.sz.springbootsample.demo.dto.ResponseResultDTO;
import com.sz.springbootsample.demo.enums.ResponseCodeEnum;
import com.sz.springbootsample.demo.form.UploadFileForm;
import com.sz.springbootsample.demo.service.UploadFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * this module only use for CD
 *
 * @author yanghaojia CD-Group
 * @date 2022/9/8 16:53
 */
@RestController
@Validated
@RequestMapping("/demo/file")
@Api(tags = "文件控制器")
public class FileController {

    @Resource
    private UploadFileService uploadFileService;

    @PostMapping("/upload")
    @IgnoreTracing
    @ApiOperation("上传")
    public ResponseResultDTO add(@ApiParam(name = "上传文件表单", value = "json格式", required = true) @Validated @RequestBody UploadFileForm form) {
        return ResponseResultDTO.ok(ResponseCodeEnum.OK.getCode(), ResponseCodeEnum.OK.getMsg(), uploadFileService.upload(form));
    }
}
