package com.rudra.aks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SprintBootInit {

	public static void main(String[] args) {
		
		SpringApplication.run(DatabaseConfig.class, args);
	}

}
