package com.nagarro.bankappbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	@Autowired
	private AuthenticationProvider authenticationProvider;

	private String[] whiteListUrls = { "/api/auth/**"};

	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http.cors().and().csrf().disable().authorizeHttpRequests() // in every app we have a white list(i.e.
																			// which dont required authentication)
				.requestMatchers(whiteListUrls).permitAll().requestMatchers("/api/admin/**").hasAuthority("SUPERVISOR")
                .requestMatchers("/api/user/**").hasAnyAuthority("USER").anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
}
