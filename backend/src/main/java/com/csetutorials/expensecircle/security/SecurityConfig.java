package com.csetutorials.expensecircle.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // This is the only reason we added the starter
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		http
			// 1. Disable CSRF (Cross-Site Request Forgery) protection
			// REST APIs using JWT don't need this; it just blocks POST requests otherwise.
			.csrf(AbstractHttpConfigurer::disable)

			// 2. Set session to Stateless
			// This ensures Spring doesn't try to create "JSESSIONID" cookies.
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			// 3. Permit all requests
			// This stops Spring Security from acting as a URL-level gatekeeper.
			// Your custom AuthenticationFilter remains the only judge of who is valid.
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll()
			);

		return http.build();
	}
}
