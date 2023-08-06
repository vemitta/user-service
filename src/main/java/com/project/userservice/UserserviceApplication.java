package com.project.userservice;

import com.project.userservice.auth.AuthenticationService;
import com.project.userservice.auth.RegisterRequest;
import com.project.userservice.models.Role;
import com.project.userservice.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService authenticationService,
//			UserService userService
//	) {
//		return args -> {
//			var admin = RegisterRequest.builder()
//					.name("Admin")
//					.email("admin@gmail.com")
//					.password("adminpassword")
//					.role(Role.ADMIN)
//					.build();
//			System.out.println("Admin token:" + authenticationService.register(admin).getToken());
//
//			var employee = RegisterRequest.builder()
//					.name("Employee")
//					.email("employee@gmail.com")
//					.password("emppassword")
//					.role(Role.EMPLOYEE)
//					.build();
//			System.out.println("Employee token:" + authenticationService.register(employee).getToken());
//
//			var recruiter = RegisterRequest.builder()
//					.name("Recruiter")
//					.email("recruiter@gmail.com")
//					.password("recruiterpassword")
//					.role(Role.RECRUITER)
//					.build();
//			System.out.println("Recruiter token:" + authenticationService.register(recruiter).getToken());
//
//			System.out.println("----------");
//			System.out.println(userService.getUser("employee@gmail.com"));
//			System.out.println("----------");
//
////			System.out.println("I'm executing before the application starts");
//		};
//	}
}
