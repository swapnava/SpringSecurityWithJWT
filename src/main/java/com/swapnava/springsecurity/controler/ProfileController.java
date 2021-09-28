package com.swapnava.springsecurity.controler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapnava.springsecurity.model.Profile;
import com.swapnava.springsecurity.model.Role;
import com.swapnava.springsecurity.service.ProfileServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProfileController {
	private final ProfileServiceImpl profileService;
	
	@GetMapping("/profiles")
	public ResponseEntity<List<Profile>> getProfiles(){
		return ResponseEntity.ok().body(profileService.getProfiles());
	}
	
	@GetMapping("/token/refresh")
	public void getNewToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String refreshToken = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refreshToken);
				String url = decodedJWT.getIssuer();
				String username = decodedJWT.getSubject();
				Profile profile = profileService.getProfile(username);
				String accessToken = JWT.create()
						.withSubject(profile.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000))
						.withIssuer(url)
						.withClaim("roles", profile.getRole())
						.sign(algorithm);
				System.out.println(accessToken);
				Map<String, String> tokens = new HashMap<>();
				tokens.put("accessToken", accessToken);
				tokens.put("refreshToken", refreshToken);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			} catch (Exception e) {
				response.setHeader("Error", e.getMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
				Map<String, String> error = new HashMap<String,String>();
				error.put("error_message", e.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		}
		else {
			throw new RuntimeException("Refresh Token is missing");
		}
	}
}
