package com.skillforge.backend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class AmazonS3Config {

    @Value("${accesskey}")
    private String accessKey;

    @Value("${secretkey}")
    private String secretKey;

    @Value("${bucketname}")
    private String bucketName;

    @Autowired
    ObjectMetadata objectMetadata;

    @Autowired
    ObjectMapper objectMapper;

    private AmazonS3 amazonS3Client;

    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        amazonS3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

    }

    public String uploadFiles(String courseDirectory, String moduleName, MultipartFile file) throws IOException {
        String key = courseDirectory+"/"+moduleName+"/"+file.getOriginalFilename();
        amazonS3Client.putObject(bucketName,key,file.getInputStream(),objectMetadata);
        return key;
    }

    public InputStreamResource getFile(String key) throws IOException {
        S3Object document = amazonS3Client.getObject(bucketName,key);
        S3ObjectInputStream inputStream = document.getObjectContent();
        return new InputStreamResource(inputStream);
    }

    public boolean deleteFile(String key) {
        amazonS3Client.deleteObject(bucketName,key);
        return true;
    }

}
