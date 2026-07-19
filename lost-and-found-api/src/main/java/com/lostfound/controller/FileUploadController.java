package com.lostfound.controller;

import com.lostfound.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Value("${lostfound.upload.path}")
    private String uploadPath;

    @PostMapping
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }
        String originalName = file.getOriginalFilename();
        String ext = originalName != null ? originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
        String fileName = UUID.randomUUID().toString() + ext;

        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        try {
            file.transferTo(new File(uploadPath + fileName));
            return Result.success("/uploads/" + fileName);
        } catch (IOException e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}
