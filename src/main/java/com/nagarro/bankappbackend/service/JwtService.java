package com.nagarro.bankappbackend.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nagarro.bankappbackend.entity.User;

@Service
public interface JwtService 
{
	public String extractUsername(String token);
	
	public String generateToken(Map<String, Object> extractClaims, UserDetails user);
	
	public boolean tokenValid(String token, UserDetails user);
	
	public boolean isTokenExpired(String token);

	public String generateToken(User user);
	
}
