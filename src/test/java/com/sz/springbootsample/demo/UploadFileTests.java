package com.sz.springbootsample.demo;

import com.sz.springbootsample.demo.dto.ResponseResultDTO;
import com.sz.springbootsample.demo.enums.ResponseCodeEnum;
import com.sz.springbootsample.demo.exception.BaseException;
import com.sz.springbootsample.demo.form.UploadFileForm;
import com.sz.springbootsample.demo.util.JSONUtils;
import com.sz.springbootsample.demo.util.Md5Utils;
import com.sz.springbootsample.demo.util.OkHttpClientUtils;
import com.sz.springbootsample.demo.util.RetryUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;

/**
 * this module only use for CD
 *
 * @author yanghaojia CD-Group
 * @date 2022/9/8 17:25
 */
@Slf4j
public class UploadFileTests {

    @Test
    public void testUploadFile() {
        String fileName = "spring-demo.jar";
        String fileVersion = "v0.0.1";
        String filePath = "/Users/youngzen/Desktop/spring-demo/v0.0.1/spring-demo.jar";

        OkHttpClient okHttpClient = OkHttpClientUtils.createHttpClient("admin", "admin");
        try {
            String uploadId = doUploadFile(okHttpClient, filePath, fileName, fileVersion);
            log.info(uploadId);
        } catch (Exception e) {
            log.error("doUploadFile error: ", e);
        }
    }

    private static String doUploadFile(OkHttpClient okHttpClient, String filePath, String fileName, String fileVersion) throws IOException {
        File file = new File(filePath);
        int shardSize = 360 * 1024;
        long size = file.length();
        int shardTotal = (int) Math.ceil((double) size / shardSize);
        int lastShardSize = (int) (size % shardSize);
        if (lastShardSize == 0) {
            lastShardSize = shardSize;
        }

        System.out.println(size);
        System.out.println(shardTotal);
        System.out.println(shardSize);
        System.out.println(lastShardSize);

        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] shardBytes = new byte[shardSize];
            String uploadId = null;
            for (int shardIndex = 1; shardIndex <= shardTotal; shardIndex++) {
                if (shardIndex == shardTotal) {
                    shardBytes = new byte[lastShardSize];
                }
                fileInputStream.read(shardBytes);
                UploadFileForm form = new UploadFileForm();
                form.setFileName(fileName);
                form.setFileVersion(fileVersion);
                form.setFileContent(Base64.getEncoder().encodeToString(shardBytes));
                form.setFileSeg(shardIndex);
                form.setFileSegs(shardTotal);
                form.setUploadId(uploadId);
                form.setChunkSize(shardSize);

                if (shardIndex == shardTotal) {
                    form.setMd5(Md5Utils.getFileMd5(filePath));
                }
                uploadId = RetryUtils.retry(() -> {
                    try {
                        return uploadFile(okHttpClient, form);
                    } catch (Exception e) {
                        log.error("uploadFile error: ", e);
                        // forces a retry
                        throw new IllegalStateException(e);
                    }
                }, 4, Duration.ofMillis(1000), false);
            }
            return uploadId;
        }
    }

    private static String uploadFile(OkHttpClient okHttpClient, UploadFileForm form) throws IOException {
        String body = JSONUtils.writeValueAsString(form);
        String response = OkHttpClientUtils.postJson(okHttpClient, "http://127.0.0.1:8080/demo/file/upload", body);
        ResponseResultDTO responseResultDTO = JSONUtils.readValue(response, ResponseResultDTO.class);
        if (!Objects.equals(responseResultDTO.getCode(), ResponseCodeEnum.OK.getCode())) {
            throw new BaseException(responseResultDTO.getCode(), responseResultDTO.getMsg());
        }
        return (String) responseResultDTO.getData();
    }
}
