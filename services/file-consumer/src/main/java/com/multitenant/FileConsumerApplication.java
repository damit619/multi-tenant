package com.multitenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FileConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileConsumerApplication.class, args);
	}

}
