package com.golfclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class GolfClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(GolfClubApplication.class, args);

		// Display running application links
		System.out.println("\n-----------------------------------------------");
		System.out.println("Golf Club Application is running!");
		System.out.println("Local:      http://localhost:8080");
		System.out.println("Health:     http://localhost:8080/actuator/health");
		System.out.println("Database:   jdbc:mysql://localhost:3306/golfclub");
		System.out.println("-----------------------------------------------");
	}
}