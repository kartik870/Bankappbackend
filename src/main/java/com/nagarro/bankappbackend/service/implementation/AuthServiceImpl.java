package com.nagarro.bankappbackend.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nagarro.bankappbackend.dto.LoginDto;
import com.nagarro.bankappbackend.dto.SignUpDto;
import com.nagarro.bankappbackend.entity.Role;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.repositories.UserRepository;
import com.nagarro.bankappbackend.response.AuthenticationResponse;
import com.nagarro.bankappbackend.service.JwtService;
import com.nagarro.bankappbackend.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepo;
	private final PasswordEncoder encoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@Autowired
	public AuthServiceImpl(UserRepository userRepo, PasswordEncoder encoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.userRepo = userRepo;
		this.encoder = encoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public User signUp(SignUpDto signupDetails) {
		if (signupDetails.getRole().toLowerCase().equals("supervisor")) {
			var user = User.builder().firstName(signupDetails.getFirstName()).lastName(signupDetails.getLastName())
					.email(signupDetails.getEmail()).password(encoder.encode(signupDetails.getPassword()))
					.role(Role.SUPERVISOR).valid(true).build();
			return userRepo.save(user);
			}
		else {
				var user = User.builder().firstName(signupDetails.getFirstName()).lastName(signupDetails.getLastName())
						.email(signupDetails.getEmail()).password(encoder.encode(signupDetails.getPassword()))
						.role(Role.USER).valid(false).build();
				userRepo.save(user);
				
				return userRepo.save(user);
		}
		
		
	}

	@Override
	public AuthenticationResponse login(LoginDto logindetails) {

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(logindetails.getEmail(), logindetails.getPassword()));
		var user = userRepo.findByEmail(logindetails.getEmail()).orElseThrow();

		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).valid(user.isValid()).Role(user.getRole().toString()).build();

	}
}
