package com.gtech.food_api;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.gtech.food_api.infra.repository.specification.CustomJpaRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class) // definindo nosso jpa como default
public class FoodApiApplication {

	public static void main(String[] args) {
		// configurando o timezone para UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(FoodApiApplication.class, args);
	}

}
