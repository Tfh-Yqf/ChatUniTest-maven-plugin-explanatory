package com.example.spring.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@RestController
@CrossOrigin
public class ZipUploadController {

    public static final String UPLOAD_DIR = "uploadfile";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAndUnzip(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {
            // 获取上传文件的原始文件名
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // 设置上传文件的存储路径
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();

            // 如果存储路径不存在，则创建它
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 将文件保存到指定路径
            Path targetPath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 解压缩 ZIP 压缩包
            unzip(targetPath.toString(), uploadPath.resolve(fileName.substring(0,fileName.indexOf("."))).toString());

            // 返回上传成功的消息
            return ResponseEntity.ok("File uploaded and unzipped successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload and unzip file");
        }
    }

    public static void unzip(String zipFilePath, String destDir) throws IOException {
        File destDirectory = new File(destDir);
        if(destDirectory.exists()){
            destDirectory.delete();
            destDirectory.mkdirs();
        }


        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                String entryPath = destDirectory.toPath().toString()+File.separator+entryName;

                if (entry.isDirectory()) {
                    Files.createDirectories(Paths.get(entryPath));
                } else {
                    if (Files.exists(Paths.get(entryPath))) Files.delete(Paths.get(entryPath));

                    Files.copy(zipFile.getInputStream(entry), Paths.get(entryPath));
                }
            }
        }
    }
}
