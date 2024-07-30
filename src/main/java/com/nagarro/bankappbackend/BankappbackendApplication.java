package com.nagarro.bankappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.nagarro.bankappbackend.repositories")
public class BankappbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankappbackendApplication.class, args);
	}

}
