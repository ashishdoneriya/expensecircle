package com.csetutorials.expensecircle.services;

import com.csetutorials.expensecircle.beans.UserInfo;
import com.csetutorials.expensecircle.exceptions.JWTDeserializationException;
import com.csetutorials.expensecircle.exceptions.JWTSerializationException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JWTService {

	private final MACSigner signer;
	private final MACVerifier verifier;

	@SneakyThrows
	public JWTService() {
      /*  SecureRandom random = new SecureRandom();
        byte[] sharedSecret = new byte[32];
        random.nextBytes(sharedSecret); */
		byte[] sharedSecret = "aasddsfsdasdfasdfasdfasdfasdfasdffasdfasdfdtest".getBytes();
		this.signer = new MACSigner(sharedSecret);
		this.verifier = new MACVerifier(sharedSecret);
	}

	public String serialize(UserInfo userInfo) {

		JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256),
			new Payload(Map.of("userId", userInfo.getUserId(), "name", userInfo.getName(), "email", userInfo.getEmail(), "picture", userInfo.getPicture())));
		try {
			jwsObject.sign(signer);
		} catch (JOSEException e) {
			throw new JWTSerializationException(e);
		}
		return jwsObject.serialize();
	}

	public UserInfo deserialize(String token) throws JWTDeserializationException {
		try {
			JWSObject jwsObject = JWSObject.parse(token);
			if (!jwsObject.verify(verifier)) {
				throw new Exception("Cannot verify jwt object");
			}
			Map<String, Object> map = jwsObject.getPayload().toJSONObject();
			return new UserInfo((String) map.get("userId"), (String) map.get("name"), (String) map.get("email"), (String) map.get("picture"));
		} catch (Exception e) {
			throw new JWTDeserializationException(e);
		}
	}

}
