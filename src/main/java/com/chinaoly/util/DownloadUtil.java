package com.chinaoly.util;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

public class DownloadUtil {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(DownloadUtil.class);

    /**
     * @param filePath   要下载的文件路径
     * @param returnName 返回的文件名
     * @param response   HttpServletResponse
     * @param delFlag    是否删除文件
     */
    public void download(String filePath, String returnName, HttpServletResponse response, HttpServletRequest request, boolean delFlag) {
        this.prototypeDownload(new File(filePath), returnName, response, request, delFlag);
    }


    /**
     * @param file       要下载的文件
     * @param returnName 返回的文件名
     * @param response   HttpServletResponse
     * @param delFlag    是否删除文件
     */
    public void download(File file, String returnName, HttpServletResponse response, HttpServletRequest request, boolean delFlag) {
        this.prototypeDownload(file, returnName, response, request, delFlag);
    }

    /**
     * @param file       要下载的文件
     * @param returnName 返回的文件名
     * @param response   HttpServletResponse
     * @param delFlag    是否删除文件
     */
    public void prototypeDownload(File file, String returnName, HttpServletResponse response, HttpServletRequest request, boolean delFlag) {
        // 下载文件
        FileInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            if (!file.exists()) {
                return;
            }
            response.reset();
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            //chrome头也包含safari,需要排除chrome
            if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
                // name.getBytes("UTF-8")处理safari的乱码问题
                byte[] bytes = userAgent.contains("MSIE") ? returnName.getBytes() : returnName.getBytes("UTF-8");
                // 各浏览器基本都支持ISO编码
                returnName = new String(bytes, "ISO-8859-1");
                response.setHeader("Content-Disposition", "attachment; filename=" + returnName);
            } else {
                //attachment作为附件下载；inline客户端机器有安装匹配程序，则直接打开；注意改变配置，清除缓存，否则可能不能看到效果
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(returnName, "UTF-8"));
            }
            //设置响应类型    PDF文件为"application/pdf"，WORD文件为："application/msword"， EXCEL文件为："application/vnd.ms-excel"。
            response.setContentType("application/octet-stream;charset=utf-8");
//          文件名外的双引号处理firefox的空格截断问题
//          response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName));
            //将文件读入响应流
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            int length = 4096;
            int readLength = 0;
            byte buf[] = new byte[4096];
            readLength = inputStream.read(buf, 0, length);
            while (readLength != -1) {
                outputStream.write(buf, 0, readLength);
                readLength = inputStream.read(buf, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("download error!");
        } finally {
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //删除原文件
            if (delFlag) {
                file.delete();
            }
        }
    }
}
