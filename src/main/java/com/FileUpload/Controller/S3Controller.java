package com.FileUpload.Controller;

import com.FileUpload.Services.ImageUploder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

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
}
