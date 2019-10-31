package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ConfigmapReloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigmapReloadApplication.class, args);
	}
	
}

@RestController
@RefreshScope
class TestController {
	
	@Value("${message.hello}")
	private String hello;
	
	@GetMapping("/")
	public String hello() {
		return hello;
	}
}
