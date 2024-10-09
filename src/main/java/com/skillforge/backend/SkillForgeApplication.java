package com.skillforge.backend;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SkillForgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillForgeApplication.class, args);
	}

	@Bean
	ObjectMetadata objectMetadata() {
		return new ObjectMetadata();
	}

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}
