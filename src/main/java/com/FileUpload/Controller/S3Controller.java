package com.FileUpload.Controller;

import com.FileUpload.Services.ImageUploder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    @Autowired
    private AmazonS3 client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @Autowired
    private ImageUploder imageUploder;

    public S3Controller(ImageUploder imageUploder) {
        this.imageUploder = imageUploder;
    }

    //Upload Image
    @PostMapping
    public ResponseEntity<?>uploadImage(@RequestParam("image")MultipartFile image){
        if(image.isEmpty()){
            return ResponseEntity.badRequest().body("Image File is required");
        }
        return ResponseEntity.ok(imageUploder.uploadImageFile(image));
    }

    @GetMapping("/download/{S3key}")
    public ResponseEntity<String>DownloadFile(@PathVariable String S3key){
        try{//retrieve file form S3 bucket
            String preSignedUrl = imageUploder.preSignedUrl(S3key);
            return ResponseEntity.ok(preSignedUrl);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
