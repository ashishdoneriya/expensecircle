package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.beans.UserInfo;
import com.csetutorials.expensecircle.services.AsyncCalls;
import com.csetutorials.expensecircle.services.GoogleTokenVerifier;
import com.csetutorials.expensecircle.services.JWTService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {

	@Autowired
	private JWTService jwtService;

	@Autowired
	private GoogleTokenVerifier googleTokenVerifier;

	@Autowired
	private AsyncCalls asyncCalls;

	@PostMapping("google-id-token")
	public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> body) {
		String idToken = body.get("googleIdToken");
		GoogleIdToken token;

		try {
			// Verify the Google ID token
			token = googleTokenVerifier.verify(idToken);

			// If the token is null, return an error response
			if (token == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(createErrorResponse("Invalid ID token. Authentication failed."));
			}

		} catch (GeneralSecurityException | IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(createErrorResponse("Error verifying ID token: " + e.getMessage()));
		}

		// Extract user information
		String email = token.getPayload().getEmail();
		String name = (String) token.getPayload().get("name");
		String picture = (String) token.getPayload().get("picture");
		// Generate a JWT token for the authenticated user
		Map<String, Object> response = new HashMap<>();
		response.put("name", name);
		response.put("email", email);
		response.put("picture", picture);
		response.put("serverAuthToken", jwtService.serialize(new UserInfo(name, email, picture)));

		asyncCalls.updateUserInfo(email, name, picture);

		// Return the user information and JWT token
		return ResponseEntity.ok(response);
	}

	// Helper method to create an error response
	private Map<String, String> createErrorResponse(String message) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", message);
		return errorResponse;
	}

}