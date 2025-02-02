package com.FileUpload.Services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploder {
    String uploadImageFile(MultipartFile image);
    List<String>AllFiles();
    String preSignedUrl(String FileName);
}
