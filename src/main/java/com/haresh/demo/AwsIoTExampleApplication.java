package com.haresh.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class AwsIoTExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsIoTExampleApplication.class, args);
	}
//ServletRegistrationBean
}

