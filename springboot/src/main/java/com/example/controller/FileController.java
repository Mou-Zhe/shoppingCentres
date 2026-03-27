package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件上传/下载控制器
 * 修复点：
 * 1. 新增@RestController + @CrossOrigin，解决接口注册和跨域问题
 * 2. 优化文件路径拼接，避免跨平台问题
 * 3. 增强异常处理，返回友好提示
 */
@RestController // 核心：添加该注解，注册为REST控制器（解决404）
@CrossOrigin("*") // 解决前端跨域上传问题
@RequestMapping("/files") // 可选：统一接口前缀，也可省略（保持/upload路径）
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    // 优化：使用系统兼容的路径分隔符，避免Windows/Linux路径问题
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @Value("${fileBaseUrl:http://localhost:9090}") // 配置默认值，避免空值
    private String fileBaseUrl;

    /**
     * 文件上传接口（POST /files/upload 或 POST /upload）
     * 若保留@RequestMapping("/files")，路径是/files/upload；若删除，路径是/upload
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) { // 明确参数名，匹配前端
        // 1. 校验文件是否为空
        if (file.isEmpty()) {
            return Result.error("上传失败：文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        try {
            // 2. 创建文件目录（Hutool的FileUtil已处理目录存在性）
            FileUtil.mkdir(filePath);

            // 3. 生成唯一文件名（时间戳+原文件名，避免重复）
            fileName = System.currentTimeMillis() + "-" + fileName;
            String realFilePath = filePath + fileName;

            // 4. 保存文件（Hutool的writeBytes更稳定）
            FileUtil.writeBytes(file.getBytes(), realFilePath);

            // 5. 拼接文件访问URL（处理fileBaseUrl末尾的/，避免双斜杠）
            String cleanBaseUrl = fileBaseUrl.endsWith("/") ? fileBaseUrl : fileBaseUrl + "/";
            String url = cleanBaseUrl + "files/download/" + fileName;

            return Result.success(url); // 返回文件访问URL

        } catch (Exception e) {
            log.error(fileName + "--文件上传失败", e);
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 文件下载接口（GET /files/download/{fileName}）
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                // 设置响应头，解决文件下载中文乱码
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
                response.setContentType("application/octet-stream");

                // 读取文件并写入响应流
                byte[] bytes = FileUtil.readBytes(filePath + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            log.warn("文件下载失败：" + fileName, e);
        }
    }
}
