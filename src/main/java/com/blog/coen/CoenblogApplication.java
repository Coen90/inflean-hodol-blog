package com.blog.coen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CoenblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoenblogApplication.class, args);
	}

}
