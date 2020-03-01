package com.chinaoly.controller;


import com.chinaoly.frm.core.entity.Result;
import com.chinaoly.frm.core.entity.ResultGenerator;
import com.chinaoly.service.FileUploadServcice;
import com.chinaoly.util.DateUtils;
import com.chinaoly.util.DownloadUtil;
import com.chinaoly.util.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import static com.chinaoly.util.DateUtils.YMDN_P;


/**
 * @author liuq
 * @date 2019-11-07 17:09:05
 */
@Controller
@RequestMapping(value = "/api/fileUpload")
@Api(value = "fileUploadExecl", tags = "上传Execl文件API", description = "鞋类和门票类清单上传详细描述")
//实现跨域注解
//origin="*"代表所有域名都可访问
//maxAge飞行前响应的缓存持续时间的最大年龄，简单来说就是Cookie的有效期 单位为秒
//若maxAge是负数,则代表为临时Cookie,不会被持久化,Cookie信息保存在浏览器内存中,浏览器关闭Cookie就消失
//allowCredentials允许带cookie的请求
//@CrossOrigin(origins = "*", maxAge = 3600)
public class FileUploadExeclController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(FileUploadExeclController.class);

    private static String filePath;

    private static String shoesPath;

    private static String ticketPath;

    private static String wordPath;

    @Autowired
    private FileUploadServcice fileUploadServcice;

//    @Autowired
//    private DocumentConverter converter;


    @ResponseBody
    @PostMapping(value = "/uploadExecl", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "uploadExecl", notes = "上传鞋类Execl格式信息")
    public Result addBlacklist(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request, String flag, Integer status) {
        //判断上传内容是否符合要求
        String fileName = multipartFile.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            return ResultGenerator.genFailResult("上传的文件格式不正确");
        }
        String file = saveFile(multipartFile, request, flag);
        int result = 0;
        try {
            if (flag.contains("shoes")) {
                result = fileUploadServcice.addFileListsByShoes(file, status);
            } else if (flag.contains("ticket")) {
                result = fileUploadServcice.addFileListsByTicket(file, status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult(result);
    }

    private String saveFile(MultipartFile multipartFile, HttpServletRequest request, String flag) {
        String path;
        String uploadFileName = multipartFile.getOriginalFilename();// 判断文件类型
        // 重命名上传后的文件名
        String fileName = MD5Util.getMD5("");
        fileName += uploadFileName.substring(uploadFileName.lastIndexOf("."));
        String subPath = DateUtils.format(new Date(), YMDN_P);
//        本地
//        String filePath = request.getSession().getServletContext().getRealPath("/");
//        服务器
        String realPath = filePath + flag + "/";
        File dir = new File(realPath + subPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String trueFileName = fileName;
        // 设置存放Excel文件的路径
        path = realPath + subPath + "/" + trueFileName;
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        try {
            multipartFile.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 下载execl模版
     *
     * @param response
     * @param request
     * @return
     */
    @GetMapping(value = "/downloadExeclTemplate", headers = "Accept=application/octet-stream")
    @ApiOperation(value = "downloadExeclTemplate", notes = "下载execl模版", produces = "application/octet-stream")
    public Result downloadExeclModel(HttpServletResponse response, HttpServletRequest request, String flag) {
        //获取模板存放的路径
//        String filePath = request.getSession().getServletContext().getRealPath("/file/"); // 本地
        String fileTemplatePath = "";
        DownloadUtil dUtil = new DownloadUtil();
        if (flag.contains("shoes")) {
            fileTemplatePath = filePath + shoesPath; // 服务器
            dUtil.download(fileTemplatePath, shoesPath, response, request, false);
        } else if (flag.contains("ticket")) {
            fileTemplatePath = filePath + ticketPath; // 服务器
            dUtil.download(fileTemplatePath, ticketPath, response, request, false);

        }
        return ResultGenerator.genSuccessResult("模版下载完成");
    }


    /**
     * @param
     * @throws Exception
     */
    /*@GetMapping(value = "/templateWord", headers = "Accept=application/octet-stream")
    @ApiOperation(value = "templateWord", notes = "生成word模版", produces = "application/octet-stream")
    public Result exportContractFromTemplate(HttpServletResponse response, HttpServletRequest request) throws Exception {
        Long start = System.currentTimeMillis(); // 记录下总时长 可以忽略
        Map<String, String> map = new LinkedHashMap<>();
//        List<UserInfo> list1 = userInfoService.selectUserInfoByNa("孙尚香");//根据员工姓名查询员工信息

        String filePath = request.getSession().getServletContext().getRealPath("/file/"); // 本地
        String srcPath = filePath + wordPath; // 服务器 // 模板路径
        String destPath = filePath + "template.docx"; // 导出路径
        // System.currentTimeMillis() ，可去掉
        InputStream inputStream = new FileInputStream(srcPath);
        FileOutputStream outputStream = new FileOutputStream(destPath);

        map.put("membercode", MD5Util.getMD5(""));
        map.put("name", "孙尚香");
        map.put("sex", "男");
        map.put("birthday", "2000-01-01");
        map.put("personid", "123948291929923838391");
        map.put("unitname", "北京");
        map.put("department", "计量中心");
        map.put("teamtype", "班员");
        map.put("postname", "初级作业员");
        map.put("postclass", "技能类");
        map.put("postseries", "营销技能序列");
        map.put("workage", "10");
        map.put("politicaloutlook", "刺客");
        map.put("employform", "劳务派遣制");
        map.put("fulltimeeducation", "博士");
        map.put("fulltimegradschool", "王者大学堂");
        map.put("fulltimemajor", "刺客学");
        map.put("fulltimedegree", "博士学位");
        map.put("marriage", "是");
        map.put("employstatus", "正常");

        WordTemplateUtil.replaceText(inputStream, outputStream, map);//通过此方法来将map中的数据添加到模板中
        inputStream.close();
        outputStream.close();

        File file = new File(destPath);
        File pdfFile = new File(filePath + "template.pdf");
        converter.convert(file).to(pdfFile).execute();
        if (file.exists()) {
            file.delete();
        }
        DownloadUtil dUtil = new DownloadUtil();
        dUtil.download(pdfFile, "template.pdf", response, request, true);
        Long end = System.currentTimeMillis();
        System.out.println(end - start);
        return ResultGenerator.genSuccessResult("模版下载完成");
    }
*/
    @Value("${file.paths}")
    public void setImagePath(String paths) {
        filePath = paths;
    }

    @Value("${file.shoseTemplate}")
    public void setShoseTemplate(String shoseTemplate) {
        shoesPath = shoseTemplate;
    }

    @Value("${file.ticketTemplate}")
    public void setTicketTemplate(String ticketTemplate) {
        ticketPath = ticketTemplate;
    }

    @Value("${file.wordTemplate}")
    public void setWordTemplate(String wordTemplate) {
        wordPath = wordTemplate;
    }
}
