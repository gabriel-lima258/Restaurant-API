package com.gtech.food_api;

import com.gtech.food_api.infrastructure.repository.specification.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class) // definindo nosso jpa como default
public class FoodApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodApiApplication.class, args);
	}

}
