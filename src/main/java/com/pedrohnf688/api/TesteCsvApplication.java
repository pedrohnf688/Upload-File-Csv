package com.pedrohnf688.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.pedrohnf688.api")
@SpringBootApplication
public class TesteCsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(TesteCsvApplication.class, args);
	}

}
