package com.csetutorials.expensecircle.config;

import com.csetutorials.expensecircle.filters.AuthenticationFilter;
import com.csetutorials.expensecircle.filters.GroupAuthorizationFilter;
import com.csetutorials.expensecircle.filters.InlineUrlRewriteFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Autowired
	private AuthenticationFilter authenticationFilter;
	@Autowired
	private GroupAuthorizationFilter groupAuthorizationFilter;

	@Bean
	public FilterRegistrationBean<AuthenticationFilter> jwtFilter() {
		FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

		// Register the JwtAuthenticationFilter
		registrationBean.setFilter(authenticationFilter);

		registrationBean.addUrlPatterns("/api/user/*",
			"/api/groups/*");
		registrationBean.setOrder(1);
		registrationBean.setDispatcherTypes(
			DispatcherType.REQUEST,
			DispatcherType.FORWARD
		);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<GroupAuthorizationFilter> groupFilterRegistration() {
		FilterRegistrationBean<GroupAuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(groupAuthorizationFilter);

		registrationBean.addUrlPatterns("/api/groups/*");

		registrationBean.setOrder(2);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<InlineUrlRewriteFilter> urlRewriteFilter() {
		FilterRegistrationBean<InlineUrlRewriteFilter> filterRegistrationBean = new FilterRegistrationBean<>(new InlineUrlRewriteFilter());
		filterRegistrationBean.setName("InlineUrlRewriteFilter");
		filterRegistrationBean.addUrlPatterns("/groups*"); // Apply to all routes
		filterRegistrationBean.setOrder(3); // Set order if other filters are in use
		return filterRegistrationBean;
	}

}