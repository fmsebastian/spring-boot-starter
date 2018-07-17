package com.poc.initializer.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class InitializerTestApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(InitializerTestApplication.class, args);
	}
}
