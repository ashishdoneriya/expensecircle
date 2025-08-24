package com.csetutorials.expensecircle.filters;

import com.csetutorials.expensecircle.exceptions.JWTDeserializationException;
import com.csetutorials.expensecircle.services.JWTService;
import com.csetutorials.expensecircle.services.LoggedInUserInfoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JWTService jwtService;
	@Autowired
	private LoggedInUserInfoService loggedInUserInfoService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		// Extract JWT from Authorization header
		String authorizationHeader = request.getHeader("Authorization");
		String token = null;

		if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7); // Remove "Bearer " prefix
		}

		// If token is missing or invalid, respond with 401 Unauthorized
		if (!validateToken(token)) {
			response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or missing JWT token");
			return; // Stop further processing
		}
		filterChain.doFilter(request, response);
	}

	private boolean validateToken(String token) {
		if (token == null) {
			return false;
		}
		token = token.trim();
		try {
			loggedInUserInfoService.setInfo(jwtService.deserialize(token));
		} catch (JWTDeserializationException e) {
			return false;
		}
		return true;
	}
}