package com.create.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

	SecretKey key = Keys.hmacShaKeyFor(JwtSecurityContext.JWT_KEY.getBytes());
	
	public String generateJwtToken(Authentication authentication) {
		String jwt= Jwts.builder().setIssuer("Kishan Sahu")
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 864000000))
				.claim("email", authentication.getName())
				.signWith(key)
				.compact();
		
		return jwt;
	}
	public String getEmailFromToken(String jwt) {
		jwt = jwt.substring(7);
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		String email=String.valueOf(claims.get("email"));
		return email;
	}
	
	
}
