package com.FileUpload.Services.Impl;

import com.FileUpload.Services.ImageUploder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;

@Service
public class S3ImageUploder implements ImageUploder {

    @Autowired
    private AmazonS3 client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateRandomID(){
        StringBuilder stringBuilder = new StringBuilder(5);
        for(int i =0;i<5;i++){
            int index = RANDOM.nextInt(ALPHANUMERIC.length());
            stringBuilder.append(ALPHANUMERIC.charAt(index));
        }
        return stringBuilder.toString();
    }

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

        //create 6 digit Random Id
        String randomId =generateRandomID();
        //A00001.png
        String newFileName = randomId + actualFileName.substring(actualFileName.lastIndexOf("."));
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
    public String preSignedUrl(String FileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,FileName)
                .withMethod(com.amazonaws.HttpMethod.GET)
                .withExpiration(new java.util.Date(System.currentTimeMillis()+3600*1000));
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}
