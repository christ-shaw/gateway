package com.ztev;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class ProcesskeeperApplication  {

	public static void main(String[] args) {
		SpringApplication.run(ProcesskeeperApplication.class, args);
	}

}
