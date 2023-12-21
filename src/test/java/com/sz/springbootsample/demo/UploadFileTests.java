package com.sz.springbootsample.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;

import org.junit.Test;

import com.sz.springbootsample.demo.dto.ResponseResultDTO;
import com.sz.springbootsample.demo.enums.ResponseCodeEnum;
import com.sz.springbootsample.demo.exception.BaseException;
import com.sz.springbootsample.demo.form.UploadFileForm;
import com.sz.springbootsample.demo.util.JSONUtils;
import com.sz.springbootsample.demo.util.Md5Utils;
import com.sz.springbootsample.demo.util.OkHttpClientUtils;
import com.sz.springbootsample.demo.util.RSAUtils;
import com.sz.springbootsample.demo.util.RetryUtils;

import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

/**
 * this module only use for CD
 *
 * @author yanghaojia CD-Group
 * @date 2022/9/8 17:25
 */
@Slf4j
public class UploadFileTests {

    public static final String FILE_SERVER_BASE_URL = "http://127.0.0.1:8080";

    @Test
    public void testUploadFile() {
        String fileName = "photo.jpg";
        String fileVersion = "v0.0.1";
        String filePath = "C:\\Users\\Zen Young\\Desktop\\photo.jpg";

        OkHttpClient okHttpClient = OkHttpClientUtils.createHttpClient("admin", "admin");
        try {
            String uploadId = doUploadFile(okHttpClient, filePath, fileName, fileVersion, true);
            log.info(uploadId);
        } catch (Exception e) {
            log.error("doUploadFile error: ", e);
        }
    }

    private static String doUploadFile(
            OkHttpClient okHttpClient,
            String filePath,
            String fileName,
            String fileVersion,
            boolean encrypt)
            throws IOException {
        String rsaPublicKey = getRsaPublicKey(okHttpClient);

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

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
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
                form.setFileContent(getFileContent(encrypt, rsaPublicKey, shardBytes));
                form.setFileSeg(shardIndex);
                form.setFileSegs(shardTotal);
                form.setUploadId(uploadId);
                form.setChunkSize(shardSize);
                form.setEncrypt(encrypt);
                form.setOverrideVersion(true);

                if (shardIndex == shardTotal) {
                    form.setMd5(Md5Utils.getFileMd5(filePath));
                }
                uploadId =
                        RetryUtils.retry(
                                () -> {
                                    try {
                                        return uploadFile(okHttpClient, form);
                                    } catch (Exception e) {
                                        log.error("uploadFile error: ", e);
                                        // forces a retry
                                        throw new IllegalStateException(e);
                                    }
                                },
                                4,
                                Duration.ofMillis(1000),
                                false);
            }
            return uploadId;
        }
    }

    private static String getFileContent(boolean encrypt, String rsaPublicKey, byte[] shardBytes) {
        if (!encrypt) {
            return Base64.getEncoder().encodeToString(shardBytes);
        }
        RSA rsa = new RSA(null, rsaPublicKey);
        return RSAUtils.encrypt(rsa, shardBytes);
    }

    private static String uploadFile(OkHttpClient okHttpClient, UploadFileForm form)
            throws IOException {
        String body = JSONUtils.writeValueAsString(form);
        String response =
                OkHttpClientUtils.postJson(
                        okHttpClient, FILE_SERVER_BASE_URL + "/api/demo/file/upload", body);
        ResponseResultDTO responseResultDTO =
                JSONUtils.readValue(response, ResponseResultDTO.class);
        if (!Objects.equals(responseResultDTO.getCode(), ResponseCodeEnum.OK.getCode())) {
            throw new BaseException(responseResultDTO.getCode(), responseResultDTO.getMsg());
        }
        return (String) responseResultDTO.getData();
    }

    private static String getRsaPublicKey(OkHttpClient okHttpClient) throws IOException {
        String response =
                OkHttpClientUtils.get(
                        okHttpClient, FILE_SERVER_BASE_URL + "/api/demo/file/getRsaPublicKey");
        ResponseResultDTO responseResultDTO =
                JSONUtils.readValue(response, ResponseResultDTO.class);
        if (!Objects.equals(responseResultDTO.getCode(), ResponseCodeEnum.OK.getCode())) {
            throw new BaseException(responseResultDTO.getCode(), responseResultDTO.getMsg());
        }
        return (String) responseResultDTO.getData();
    }
}
