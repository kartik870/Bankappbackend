package com.nagarro.bankappbackend.service.implementation;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nagarro.bankappbackend.constant.Constants;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

	public String extractUsername(String token) {

		return extractClaim(token, Claims::getSubject); // subject is the username or email of the user
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(User user) {
		return generateToken(new HashMap<>(), user);
	}

	public String generateToken(Map<String, Object> extractClaims, UserDetails user) {
		return Jwts.builder().setClaims(extractClaims).setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 *60* 2))
				.signWith(SignatureAlgorithm.HS256, getSigningKey()).compact();
	}

	public boolean tokenValid(String token, UserDetails user) {
		final String username = extractUsername(token);
		return (username.equals(user.getUsername())) && !isTokenExpired(token);
	}

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
	}
	
	private Key getSigningKey() {
	    byte[] keyBytes = Base64.getDecoder().decode(Constants.SECRET_KEY);
	    return Keys.hmacShaKeyFor(keyBytes);
	}

}
