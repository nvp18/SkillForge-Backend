package com.skillforge.backend.config;

import com.amazonaws.HttpMethod;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public String getPreSignedURL(String key) throws IOException {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 10 * 60 * 1000;
        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
                .withMethod(com.amazonaws.HttpMethod.GET)
                .withExpiration(expiration)
                .withResponseHeaders(new com.amazonaws.services.s3.model.ResponseHeaderOverrides()
                        .withContentDisposition("inline")
                        .withContentType("application/pdf"));

        URL preSignedURL = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return preSignedURL.toString();
    }

    public boolean deleteFile(String key) {
        amazonS3Client.deleteObject(bucketName,key);
        return true;
    }

    public boolean deleteCourseModules(String courseDirectory) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName)
                .withPrefix(courseDirectory)
                .withMaxKeys(4);
        ObjectListing objectListing;
        do {
            objectListing = amazonS3Client.listObjects(listObjectsRequest);
            List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
            for(S3ObjectSummary summary: objectListing.getObjectSummaries()) {
                keys.add(new DeleteObjectsRequest.KeyVersion(summary.getKey()));
            }
            if(!keys.isEmpty()) {
                DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucketName).withKeys(keys);
                amazonS3Client.deleteObjects(deleteRequest);
            }
            listObjectsRequest.setMarker(objectListing.getNextMarker());
        } while (objectListing.isTruncated());
        return true;
    }

}
