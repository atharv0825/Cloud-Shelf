package com.FileUpload.Config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3config {
    @Value("${app.s3.bucket}")
    private String bucketName;

    public void getBucketName(){
        System.out.println("Bucket Name : " + bucketName);
    }

    @Value("${cloud.aws.credentials.access-key}")
    private String AWSAccessKey ;
    @Value("${cloud.aws.credentials.secret-key}")
    private String AWSSecreteKey;
    @Value("${cloud.aws.region.static}")
    private String Region;

    @Bean
    public AmazonS3 client(){
        AWSCredentials credentials = new BasicAWSCredentials(AWSAccessKey,AWSSecreteKey);

        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Region)
                .build();

        return amazonS3;
    }
}
