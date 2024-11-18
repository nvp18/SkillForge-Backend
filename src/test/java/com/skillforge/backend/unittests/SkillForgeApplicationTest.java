package com.skillforge.backend.unittests;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillforge.backend.SkillForgeApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class SkillForgeApplicationTest {

    private SkillForgeApplication skillForgeApplication;

    @BeforeEach
    void setUp() {
        skillForgeApplication = new SkillForgeApplication();
    }

    @Test
    void testObjectMetadataBeanCreation() {
        ObjectMetadata objectMetadata = skillForgeApplication.objectMetadata();
        assertNotNull(objectMetadata);
    }

    @Test
    void testObjectMapperBeanCreation() {
        ObjectMapper objectMapper = skillForgeApplication.objectMapper();
        assertNotNull(objectMapper);
    }

    @Test
    void testLoadProperties() {
        try (MockedStatic<Dotenv> mockedDotenv = Mockito.mockStatic(Dotenv.class)) {
            Dotenv dotenvMock = mock(Dotenv.class);
            mockedDotenv.when(Dotenv::load).thenReturn(dotenvMock);

            when(dotenvMock.get("AWS_ACCESS_KEY")).thenReturn("mockAccessKey");
            when(dotenvMock.get("AWS_SECRET_KEY")).thenReturn("mockSecretKey");
            when(dotenvMock.get("AWS_S3_BUCKET_NAME")).thenReturn("mockBucketName");
            when(dotenvMock.get("SKILL_FORGE_MAIL")).thenReturn("mockMail");
            when(dotenvMock.get("SKILL_FORGE_MAIL_APP_PASSWORD")).thenReturn("mockPassword");
            when(dotenvMock.get("DATABASE_USERNAME")).thenReturn("mockDbUser");
            when(dotenvMock.get("DATABASE_PASSWORD")).thenReturn("mockDbPass");
            when(dotenvMock.get("DATABASE_NAME")).thenReturn("mockDbName");
            when(dotenvMock.get("DATABASE_URL")).thenReturn("mockDbUrl");
            when(dotenvMock.get("JWT_SECRET_KEY")).thenReturn("mockJwtKey");
            String[] args = new String[]{};
            //doNothing().when(SpringApplication.run(SkillForgeApplication.class,args));
            SkillForgeApplication.loadProperties();

            assertEquals("mockAccessKey", System.getProperty("AWS_ACCESS_KEY"));
            assertEquals("mockSecretKey", System.getProperty("AWS_SECRET_KEY"));
            assertEquals("mockBucketName", System.getProperty("AWS_S3_BUCKET_NAME"));
            assertEquals("mockMail", System.getProperty("SKILL_FORGE_MAIL"));
            assertEquals("mockPassword", System.getProperty("SKILL_FORGE_MAIL_APP_PASSWORD"));
            assertEquals("mockDbUser", System.getProperty("DATABASE_USERNAME"));
            assertEquals("mockDbPass", System.getProperty("DATABASE_PASSWORD"));
            assertEquals("mockDbName", System.getProperty("DATABASE_NAME"));
            assertEquals("mockDbUrl", System.getProperty("DATABASE_URL"));
            assertEquals("mockJwtKey", System.getProperty("JWT_SECRET_KEY"));
        }
    }
}
