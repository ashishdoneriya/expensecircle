package com.csetutorials.expensecircle.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoSessionConfig {

	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sessionCustomizer() {
		return factory -> factory.addContextCustomizers(context -> context.setSessionTimeout(0));
	}
}
