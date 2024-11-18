package com.skillforge.backend;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SkillForgeApplication {

	public static void main(String[] args) {
		loadProperties();
		SpringApplication.run(SkillForgeApplication.class, args);
	}

	@Bean
	public ObjectMetadata objectMetadata() {
		return new ObjectMetadata();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	public static void loadProperties() {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("AWS_ACCESS_KEY",dotenv.get("AWS_ACCESS_KEY"));
		System.setProperty("AWS_SECRET_KEY",dotenv.get("AWS_SECRET_KEY"));
		System.setProperty("AWS_S3_BUCKET_NAME",dotenv.get("AWS_S3_BUCKET_NAME"));
		System.setProperty("SKILL_FORGE_MAIL",dotenv.get("SKILL_FORGE_MAIL"));
		System.setProperty("SKILL_FORGE_MAIL_APP_PASSWORD",dotenv.get("SKILL_FORGE_MAIL_APP_PASSWORD"));
		System.setProperty("DATABASE_USERNAME",dotenv.get("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD",dotenv.get("DATABASE_PASSWORD"));
		System.setProperty("DATABASE_NAME",dotenv.get("DATABASE_NAME"));
		System.setProperty("DATABASE_URL",dotenv.get("DATABASE_URL"));
		System.setProperty("JWT_SECRET_KEY",dotenv.get("JWT_SECRET_KEY"));
	}

}
