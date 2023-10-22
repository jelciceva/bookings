package com.example.demo;

import com.example.demo.entities.Booking;
import com.example.demo.entities.Property;
import com.example.demo.entities.Users;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.PropertyRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.UUID;


@SpringBootApplication
@EntityScan(basePackages = "com.example.demo")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BookingRepository bookingRepository, UserRepository userRepository, PropertyRepository propertyRepository) {
		return args -> {
			bookingRepository.save(new Booking(LocalDate.now(), LocalDate.now(), UUID.fromString("6870a5e2-5ee8-4a64-85f8-26b2660e54c4"), "guest1", UUID.fromString("7e9b70df-45d2-4f67-854d-9b1f0119ec97"	)));
			bookingRepository.save(new Booking(LocalDate.now(), LocalDate.now(), UUID.fromString("6870a5e2-5ee8-4a64-85f8-26b2660e54c4"), "guest1", UUID.fromString("7e9b70df-45d2-4f67-854d-9b1f0119ec97"	)));
			userRepository.save(new Users("username1", "pass"));
			userRepository.save(new Users("username2", "pass"));
			propertyRepository.save(new Property());

		};
	}

}
