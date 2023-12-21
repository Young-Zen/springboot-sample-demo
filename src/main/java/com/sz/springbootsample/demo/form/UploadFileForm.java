package com.sz.springbootsample.demo.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * this module only use for CD
 *
 * @author yanghaojia CD-Group
 * @date 2022/9/8 15:30
 */
@Data
public class UploadFileForm {

    // 文件的 Base64 字符串
    @NotBlank(message = "文件内容不能为空")
    private String fileContent;
    // 文件分片大小
    @NotNull(message = "文件分片大小不能为空")
    @Min(value = 1, message = "最小文件分片大小为 1")
    private Integer chunkSize;
    // 当前是第几片
    @NotNull(message = "文件分片数不能为空")
    @Min(value = 1, message = "最小文件分片数为 1")
    private Integer fileSeg;
    // 文件总片数
    @NotNull(message = "文件分片总数不能为空")
    @Min(value = 1, message = "最小文件分片总数为 1")
    private Integer fileSegs;
    // 文件的 md5 值
    private String md5;
    // 文件名称
    @NotBlank(message = "文件名称不能为空")
    private String fileName;
    // 文件版本
    @NotBlank(message = "文件版本不能为空")
    private String fileVersion;
    // 此次上传过程中的 id
    private String uploadId;
    // 是否加密
    private boolean encrypt;
    // 是否覆盖版本
    private boolean overrideVersion;
}
