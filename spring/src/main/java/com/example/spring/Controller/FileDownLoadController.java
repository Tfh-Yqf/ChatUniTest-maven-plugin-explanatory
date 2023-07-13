package com.example.spring.Controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@CrossOrigin

public class FileDownLoadController {

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) throws IOException {
        // 获取文件路径
        String filePath = "/path/to/files/" + filename;

        Path uploadPath = Paths.get(ZipUploadController.UPLOAD_DIR).toAbsolutePath().normalize();
        Path dirPath = uploadPath.resolve(filename.substring(0,filename.indexOf(".")));
        String zipPath = uploadPath+File.separator+filename.substring(0,filename.indexOf("."))+"_chatunitest.zip";
        compressFolder(dirPath.toString(),zipPath);
        // 读取文件到输入流
        File file = new File(zipPath);
        InputStream inputStream = new FileInputStream(file);

        // 创建输入流资源
        Resource resource = new InputStreamResource(inputStream);

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename.substring(0,filename.indexOf("."))+"_chatunitest.zip");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // 返回响应实体
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    public static void compressFolder(String sourceFolderPath, String zipFilePath) throws IOException {
        File sourceFolder = new File(sourceFolderPath);
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath));
        addFolderToZip(sourceFolder, sourceFolder.getName(), zipOutputStream);
        zipOutputStream.close();
    }

    private static void addFileToZip(File file, String parentPath, ZipOutputStream zipOutputStream) throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = new FileInputStream(file);
        zipOutputStream.putNextEntry(new ZipEntry(parentPath + File.separator + file.getName()));
        int length;
        while ((length = fileInputStream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length);
        }
        fileInputStream.close();
    }

    private static void addFolderToZip(File folder, String parentPath, ZipOutputStream zipOutputStream) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                addFolderToZip(file, parentPath + File.separator + file.getName(), zipOutputStream);
            } else {
                addFileToZip(file, parentPath, zipOutputStream);
            }
        }
    }
}
