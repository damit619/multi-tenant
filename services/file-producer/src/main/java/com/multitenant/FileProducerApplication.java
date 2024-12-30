package com.multitenant;

import com.multitenant.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FileProducerApplication implements CommandLineRunner {

	@Autowired
	private FileStorageService fileStorageService;

	public static void main(String[] args) {
		SpringApplication.run(FileProducerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fileStorageService.storeFile("tenant-file.csv");
	}
}
