package com.sz.springbootsample.demo.util;

import com.sz.springbootsample.demo.exception.BaseException;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * this module only use for CD
 *
 * @author yanghaojia CD-Group
 * @date 2022/9/8 17:37
 */
public class Md5Utils {

    public static String getFileMd5(String filePath) {
        return getFileMd5(new File(filePath));
    }

    public static String getFileMd5(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return DigestUtils.md5DigestAsHex(fileInputStream);
        } catch (IOException e) {
            throw new BaseException("计算文件 MD5 出错: " + file.getAbsolutePath(), e);
        }
    }
}
