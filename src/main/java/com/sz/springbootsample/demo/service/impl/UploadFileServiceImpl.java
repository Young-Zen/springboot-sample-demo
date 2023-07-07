package com.sz.springbootsample.demo.service.impl;

import com.sz.springbootsample.demo.enums.ResponseCodeEnum;
import com.sz.springbootsample.demo.exception.BaseException;
import com.sz.springbootsample.demo.form.UploadFileForm;
import com.sz.springbootsample.demo.service.UploadFileService;
import com.sz.springbootsample.demo.util.Md5Utils;
import com.sz.springbootsample.demo.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.security.action.GetPropertyAction;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

import static java.security.AccessController.doPrivileged;

/**
 * this module only use for CD
 *
 * @author yanghaojia CD-Group
 * @date 2022/9/8 15:40
 */
@Service
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {

    public static final String UPLOAD_DIR_PATH;

    static {
        Path path = Paths.get(doPrivileged(new GetPropertyAction("java.io.tmpdir")));
        UPLOAD_DIR_PATH = Paths.get(path.toString(), "upload").toString();
    }

    @Override
    public String upload(UploadFileForm form) {
        createUploadDir(form);
        checkVersionExists(form);
        return saveUploadFile(form);
    }

    private String saveUploadFile(UploadFileForm form) {
        String uploadId = writeTempUploadFile(form);
        if (Objects.equals(form.getFileSegs(), form.getFileSeg())) {
            File uploadTempFile = Paths.get(UPLOAD_DIR_PATH, form.getFileName(), uploadId).toFile();
            if (!Objects.equals(form.getMd5(), Md5Utils.getFileMd5(uploadTempFile))) {
                throw new BaseException(ResponseCodeEnum.ARGUMENT_VALID_FAIL.getCode(), ResponseCodeEnum.ARGUMENT_VALID_FAIL.getMsg());
            }

            File uploadFile = Paths.get(UPLOAD_DIR_PATH, form.getFileName(), form.getFileVersion()).toFile();
            if (uploadFile.exists() && form.isOverrideVersion()) {
                uploadFile.delete();
            }
            if (!uploadTempFile.renameTo(uploadFile)) {
                throw new BaseException("saveUploadFile fail");
            }
        }
        return uploadId;
    }

    private void checkVersionExists(UploadFileForm form) {
        if (form.isOverrideVersion()) {
            return;
        }
        File uploadFile = Paths.get(UPLOAD_DIR_PATH, form.getFileName(), form.getFileVersion()).toFile();
        if (uploadFile.exists()) {
            throw new BaseException("File version already exists");
        }
    }

    private String writeTempUploadFile(UploadFileForm form) {
        String uploadId = form.getUploadId();
        if (Objects.equals(form.getFileSeg(), 1)) {
            uploadId = UUID.randomUUID().toString();
        }
        File uploadTempFile = Paths.get(UPLOAD_DIR_PATH, form.getFileName(), uploadId).toFile();
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(uploadTempFile, "rw")) {
            int offset = (form.getFileSeg() - 1) * form.getChunkSize();
            randomAccessFile.seek(offset);
            byte[] fileContent = this.getFileContent(form);
            randomAccessFile.write(fileContent);
        } catch (IOException e) {
            log.error("writeUploadFile error: ", e);
            throw new BaseException(ResponseCodeEnum.FAIL, e);
        }
        return uploadId;
    }

    private byte[] getFileContent(UploadFileForm form) {
        if (!form.isEncrypt()) {
            return Base64.getDecoder().decode(form.getFileContent().getBytes(StandardCharsets.UTF_8));
        }
        return RSAUtils.decrypt(form.getFileContent());
    }

    private void createUploadDir(UploadFileForm form) {
        try {
            Path uploadDirPath = Paths.get(UPLOAD_DIR_PATH, form.getFileName());
            Files.createDirectories(uploadDirPath);
        } catch (IOException e) {
            log.error("createUploadDir error: ", e);
            throw new BaseException(ResponseCodeEnum.FAIL, e);
        }
    }
}
