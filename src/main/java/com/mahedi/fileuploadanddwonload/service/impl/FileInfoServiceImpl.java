package com.mahedi.fileuploadanddwonload.service.impl;

import com.mahedi.fileuploadanddwonload.model.FileInfo;
import com.mahedi.fileuploadanddwonload.repository.FileInfoRepository;
import com.mahedi.fileuploadanddwonload.service.FileInfoService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileInfoServiceImpl implements FileInfoService {

  private final FileInfoRepository fileInfoRepository;

  public String uploadFile(MultipartFile file) {
    try {
      String homeDir = System.getProperty("user.home");
      String folderPath = homeDir + File.separator + "uploadedFiles";
      File directory = new File(folderPath);
      if (!directory.exists()) {
        directory.mkdir();
      }

      String filePath = folderPath + File.separator + file.getOriginalFilename();
      Path destPath = Paths.get(filePath);
      Files.write(destPath, file.getBytes());

      // Encode the path using Base64
      String encodedPath = Base64.getEncoder().encodeToString(filePath.getBytes());

      // Save file info to the database
      FileInfo fileInfo = new FileInfo();
      fileInfo.setFilePath(encodedPath);
      fileInfo.setFileName(file.getOriginalFilename());
      // Set other info

      fileInfoRepository.save(fileInfo);

      return "File uploaded successfully to: " + filePath + " and info saved to the database.";
    } catch (IOException e) {
      return "File upload failed: " + e.getMessage();
    }
  }

}
