package com.processbyte.landing;

import com.processbyte.landing.i18n.TLDLocaleResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;

@SpringBootApplication
@EnableScheduling
public class LandingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LandingApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new TLDLocaleResolver();
	}
}
