package com.bankapp.accountsmicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AccountsMicroApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountsMicroApplication.class, args);
	}
}
