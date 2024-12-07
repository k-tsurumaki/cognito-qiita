package com.example.cognito_qiita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CognitoQiitaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CognitoQiitaApplication.class, args);
	}

}
