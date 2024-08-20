package edu.allinone.sugang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class) // 시큐리티에서 제공하는 로그인 기본 화면 제거
public class SugangApplication {

	public static void main(String[] args) {
		SpringApplication.run(SugangApplication.class, args);
	}

}

