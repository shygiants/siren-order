package io.github.shygiants.sirenorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SirenorderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SirenorderApplication.class, args);
	}

}
