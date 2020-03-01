package com.chinaoly.util;

import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * MD5工具类
 *
 * @author pibigstar
 */
public class MD5Util {

    //盐，用于混交md5
    private static final String slat = "&%$¥¥@@!!***&&%%$$#@";
    /**
     * 生成md5
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        String base = str + "/" + slat + UUID.randomUUID().toString().replaceAll("-", "");
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes()).toUpperCase();
        return md5;
    }

}