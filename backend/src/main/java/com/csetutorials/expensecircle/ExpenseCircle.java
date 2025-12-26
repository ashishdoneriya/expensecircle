package com.csetutorials.expensecircle;

import com.csetutorials.expensecircle.config.FullyQualifiedBeanNameGenerator;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableScheduling
public class ExpenseCircle {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseCircle.class, args);
	}

	@Bean
	public BeanNameGenerator beanNameGenerator() {
		return new FullyQualifiedBeanNameGenerator();
	}

}
