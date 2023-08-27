package com.globe.gastronomy.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "GastronomyGlobe API", version = "1.0", description = "Gastronomy Globe"))
@EnableScheduling
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}


/**
 * ---NOTE---
 * SWAGGER API LINK -> http://localhost:8080/swagger-ui/index.html?continue#/main-api
 *
 *
 *
 *
 *
 * */