package com.nagarro.bankappbackend.entity;

import java.time.LocalDate;
import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder                                       //for the use of builder DP
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails                  //to make spring security easy to use
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	@Email
	@Column(unique = true)
	private String email;
	@NotNull
	private String password;

	private boolean valid;
	
	private LocalDate dateOfBirth;
	
	private int age;
	
	private String profilePhoto;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToOne(mappedBy = "user")
    private Account account;
	
	
//	
//		METHORDS REQUIRED BY SPRING SECURITY
//	
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}
	
	@Override
	public boolean isEnabled()
	{
		return true;
	}

}
