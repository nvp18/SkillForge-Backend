package com.skillforge.backend.unittests;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.skillforge.backend.config.AmazonS3Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AmazonS3ConfigTest {

    @Mock
    private AmazonS3 amazonS3Client;

    @Spy
    @InjectMocks
    private AmazonS3Config amazonS3Config;

    @Mock
    private ObjectMetadata objectMetadata;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Use reflection to set private fields for `@Value` fields
        setPrivateField(amazonS3Config, "accessKey", "mockAccessKey");
        setPrivateField(amazonS3Config, "secretKey", "mockSecretKey");
        setPrivateField(amazonS3Config, "bucketName", "mockBucketName");

        try (MockedStatic<AmazonS3ClientBuilder> mockedBuilder = mockStatic(AmazonS3ClientBuilder.class)) {
            AmazonS3ClientBuilder mockBuilder = mock(AmazonS3ClientBuilder.class);
            when(mockBuilder.withCredentials(any(AWSStaticCredentialsProvider.class))).thenReturn(mockBuilder);
            when(mockBuilder.withRegion(any(Regions.class))).thenReturn(mockBuilder);
            when(mockBuilder.build()).thenReturn(amazonS3Client); // Return the mocked AmazonS3 client
            mockedBuilder.when(AmazonS3ClientBuilder::standard).thenReturn(mockBuilder);

            amazonS3Config.init(); // Call the init method to use the mocked builder
        }

    }

    private void setPrivateField(Object targetObject, String fieldName, Object value) throws Exception {
        var field = targetObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(targetObject, value);
    }

    @Test
    void testUploadFiles() throws IOException {
        String courseDirectory = "courseDir";
        String moduleName = "moduleName";
        String fileName = "test.pdf";
        String expectedKey = courseDirectory + "/" + moduleName + "/" + fileName;

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn(fileName);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));
        when(amazonS3Client.putObject("mockBucketName",expectedKey,mockFile.getInputStream(),objectMetadata)).thenReturn(new PutObjectResult());

        String actualKey = amazonS3Config.uploadFiles(courseDirectory, moduleName, mockFile);

        assertEquals(expectedKey, actualKey);
        verify(amazonS3Client, times(1)).putObject(eq("mockBucketName"), eq(expectedKey), any(), eq(objectMetadata));
    }

    @Test
    void testGetPreSignedURL() throws IOException {
        String key = "test/key.pdf";
        URL mockURL = new URL("http://mockurl.com/preSignedUrl");

        when(amazonS3Client.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).thenReturn(mockURL);

        String preSignedURL = amazonS3Config.getPreSignedURL(key);

        assertEquals(mockURL.toString(), preSignedURL);
        verify(amazonS3Client, times(1)).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
    }

    @Test
    void testDeleteFile() {
        String key = "test/key.pdf";

        boolean result = amazonS3Config.deleteFile(key);

        assertTrue(result);
        verify(amazonS3Client, times(1)).deleteObject("mockBucketName", key);
    }

    @Test
    void testDeleteCourseModules() {
        String courseDirectory = "courseDir/";
        List<S3ObjectSummary> objectSummaries = Arrays.asList(
                createMockS3ObjectSummary("courseDir/file1.pdf"),
                createMockS3ObjectSummary("courseDir/file2.pdf")
        );

        ObjectListing mockListing = mock(ObjectListing.class);
        when(mockListing.getObjectSummaries()).thenReturn(objectSummaries);
        when(mockListing.isTruncated()).thenReturn(false);

        when(amazonS3Client.listObjects(any(ListObjectsRequest.class))).thenReturn(mockListing);

        boolean result = amazonS3Config.deleteCourseModules(courseDirectory);

        assertTrue(result);
        verify(amazonS3Client, times(1)).deleteObjects(any(DeleteObjectsRequest.class));
    }

    @Test
    void testDeleteCourseModules_EmptyKeys() {
        String courseDirectory = "courseDir/";

        ObjectListing mockObjectListing = mock(ObjectListing.class);

        // Mock the S3 client to return an empty list of object summaries
        when(amazonS3Client.listObjects(any(ListObjectsRequest.class))).thenReturn(mockObjectListing);
        when(mockObjectListing.getObjectSummaries()).thenReturn(Collections.emptyList()); // No objects in the directory
        when(mockObjectListing.isTruncated()).thenReturn(false); // No more objects to fetch

        boolean result = amazonS3Config.deleteCourseModules(courseDirectory);

        assertTrue(result);
        verify(amazonS3Client, times(1)).listObjects(any(ListObjectsRequest.class)); // Ensure listObjects is called
        verify(amazonS3Client, never()).deleteObjects(any(DeleteObjectsRequest.class)); // Ensure deleteObjects is never called
    }
    @Test
    void testDeleteCourseModules_FullWhileLoopCoverage() {
        String courseDirectory = "courseDir/";

        // Mock ObjectListing for first iteration
        ObjectListing mockObjectListing1 = mock(ObjectListing.class);
        S3ObjectSummary summary1 = new S3ObjectSummary();
        summary1.setKey("courseDir/file1.txt");

        when(mockObjectListing1.getObjectSummaries()).thenReturn(Arrays.asList(summary1));
        when(mockObjectListing1.isTruncated()).thenReturn(true); // Indicates more objects to list
        when(mockObjectListing1.getNextMarker()).thenReturn("marker1");

        // Mock ObjectListing for second (final) iteration
        ObjectListing mockObjectListing2 = mock(ObjectListing.class);
        S3ObjectSummary summary2 = new S3ObjectSummary();
        summary2.setKey("courseDir/file2.txt");

        when(mockObjectListing2.getObjectSummaries()).thenReturn(Arrays.asList(summary2));
        when(mockObjectListing2.isTruncated()).thenReturn(false); // No more objects to list

        when(amazonS3Client.listObjects(any(ListObjectsRequest.class)))
                .thenReturn(mockObjectListing1)
                .thenReturn(mockObjectListing2);

        boolean result = amazonS3Config.deleteCourseModules(courseDirectory);

        assertTrue(result);
        verify(amazonS3Client, times(2)).listObjects(any(ListObjectsRequest.class)); // Ensure loop runs twice
        verify(amazonS3Client, times(2)).deleteObjects(any(DeleteObjectsRequest.class)); // Ensure deleteObjects called twice
    }

    private S3ObjectSummary createMockS3ObjectSummary(String key) {
        S3ObjectSummary summary = new S3ObjectSummary();
        summary.setKey(key);
        return summary;
    }
}
