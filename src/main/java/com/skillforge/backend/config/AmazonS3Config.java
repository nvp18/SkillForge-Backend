package com.skillforge.backend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public boolean uploadFiles(String courseName, MultipartFile file) throws IOException {
        String key = courseName+"/"+file.getOriginalFilename();
        amazonS3Client.putObject(bucketName,key,file.getInputStream(),objectMetadata);
        return true;
    }

    public Map<String,Object> getFile(String courseId, String fileName) throws IOException {
        String key = courseId +"/"+fileName;
        S3Object document = amazonS3Client.getObject(bucketName,key);
        S3ObjectInputStream inputStream = document.getObjectContent();
        Map<String,Object> documentData = objectMapper.readValue(inputStream,Map.class);
        inputStream.close();
        return documentData;
    }

    public boolean deleteFile(String courseId, String fileName) {
        String key = courseId+"/"+fileName;
        amazonS3Client.deleteObject(bucketName,key);
        return true;
    }

}
