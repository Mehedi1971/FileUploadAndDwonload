package com.mahedi.fileuploadanddwonload.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileInfoService {

  String uploadFile(MultipartFile file);
}
