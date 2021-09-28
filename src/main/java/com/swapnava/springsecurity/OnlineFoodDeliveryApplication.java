package com.swapnava.springsecurity;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.swapnava.springsecurity.model.Profile;
import com.swapnava.springsecurity.model.Role;
import com.swapnava.springsecurity.service.ProfileService;

@SpringBootApplication
public class OnlineFoodDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineFoodDeliveryApplication.class, args);
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	CommandLineRunner run(ProfileService profileService) {
		return arg -> {
			profileService.saveRole(new Role(0,"ROLE_USER"));
			profileService.saveRole(new Role(0,"ADMIN"));
			profileService.saveRole(new Role(0,"API_MANAGER"));
			
			profileService.saveProfile(new Profile(0,"Swapnava Halder","swapnava@xyz.com","12345", new ArrayList()));
			profileService.saveProfile(new Profile(0,"Satabdi Saha","satabdi@xyz.com","00000", new ArrayList()));
			profileService.saveProfile(new Profile(0,"Anonymous Surname","anonymous@xyz.com","12345", new ArrayList()));
			
			profileService.addRoleToProfile("swapnava@xyz.com", "ADMIN");
			profileService.addRoleToProfile("swapnava@xyz.com", "API_MANAGER");
			profileService.addRoleToProfile("satabdi@xyz.com", "ROLE_USER");
			profileService.addRoleToProfile("anonymous@xyz.com", "ROLE_USER");
		};
	}
}
