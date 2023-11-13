package com.mahedi.fileuploadanddwonload.controller;

import com.mahedi.fileuploadanddwonload.model.FileInfo;
import com.mahedi.fileuploadanddwonload.repository.FileInfoRepository;
import com.mahedi.fileuploadanddwonload.service.FileInfoService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileInfoController {

  private final FileInfoService fileInfoService;
  private final FileInfoRepository fileInfoRepository;

  @PostMapping("/upload")
  public String handleFileUpload(@RequestParam("file") MultipartFile file) {
    return fileInfoService.uploadFile(file);
  }

  @GetMapping("/download/{fileId}")
  public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId) {
    Optional<FileInfo> fileInfoOptional = fileInfoRepository.findById(fileId);
    if (fileInfoOptional.isPresent()) {
      FileInfo fileInfo = fileInfoOptional.get();
      // Decode the Base64 encoded path
      byte[] decodedPath = Base64.getDecoder().decode(fileInfo.getFilePath());
      String filePath = new String(decodedPath);

      // Fetch the file from the given path
      File file = new File(filePath);

      try {
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(filePath)));

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileInfo.getFileName())
            .contentLength(file.length())
            .body(resource);
      } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(null); // Or handle the error appropriately
      }
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
