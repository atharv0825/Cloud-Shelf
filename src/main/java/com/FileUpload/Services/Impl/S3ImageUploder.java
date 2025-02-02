package com.FileUpload.Services.Impl;

import com.FileUpload.Exception.ImageUploderException;
import com.FileUpload.Services.ImageUploder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class S3ImageUploder implements ImageUploder {

    @Autowired
    private AmazonS3 client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadImageFile(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be null or empty");
        }

        //abc.png
        String actualFileName = image.getOriginalFilename();
        if(actualFileName == null || !actualFileName.contains(".")){
            throw new IllegalArgumentException("Invalid File Name");
        }

        //adtefas.png
        String newFileName = UUID.randomUUID().toString() + actualFileName.substring(actualFileName.lastIndexOf("."));
        //create Metadata of the file
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());

        try {
            PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucketName,newFileName,image.getInputStream(),metadata));
            return newFileName ;
        } catch (IOException e) {
            throw new RuntimeException("Error in uploading image",e);
        }
    }

    @Override
    public List<String> AllFiles() {
        return List.of();
    }

    @Override
    public String preSignedUrl() {
        return "";
    }
}
