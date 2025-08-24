package com.csetutorials.expensecircle.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleTokenVerifier {

	// Your Google OAuth Client ID
	private static final String clientId = "378687249491-55qmh3gcrv72dbhqf67bh1o09rgpr9hh.apps.googleusercontent.com";

	// Set up the HTTP transport and JSON factory
	private static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
		new NetHttpTransport(), new GsonFactory())
		.setAudience(Collections.singletonList(clientId))
		.build();

	public GoogleIdToken verify(@NonNull String sGoogleIdToken)
		throws GeneralSecurityException, IOException {
		return verifier.verify(sGoogleIdToken);
	}

}