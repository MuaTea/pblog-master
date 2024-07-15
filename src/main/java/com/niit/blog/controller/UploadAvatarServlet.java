package com.niit.blog.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RequestMapping("/upload")
@RestController
public class UploadAvatarServlet extends HttpServlet {
    @PostMapping("/cover")
    public String getUploadPath(HttpServletRequest request, MultipartFile file) throws IOException {
        // 获取当前日期
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 生成 UUID 并转换为小写
        String uuid = UUID.randomUUID().toString().toLowerCase();
        // 获取文件扩展名
        String fileExtension = getFileExtension(file.getOriginalFilename());

        // 生成新的文件名
        String fileName = currentDate + "_" + uuid + fileExtension;

        // 获取上传目录路径
        // String uploadDir = request.getServletContext().getRealPath("") + File.separator + "uploads";
        String uploadDir = "D:\\" + File.separator + "uploads";

        // 创建目录如果不存在
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // 处理文件上传
        File uploadedFile = new File(uploadDir, fileName);
        file.transferTo(uploadedFile);

        return request.getContextPath() + "/uploads/" + fileName;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    @GetMapping("/img")
    public void download(String name, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应的内容类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        // 设置文件下载目录
        String downloadDir = "D:\\";
        // 设置文件下载目录
        File file = new File(downloadDir, name);

        // 检查文件是否存在
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("File not found.");
            return;
        }

        // 通过文件输入流读取文件
        FileInputStream inputStream = new FileInputStream(file);
        // 获取响应输出流
        ServletOutputStream outputStream = response.getOutputStream();

        // 设置响应头，提示浏览器以附件形式下载文件
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));

        // 处理下载流复制
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        // 关闭流
        outputStream.close();
        inputStream.close();
    }
}
