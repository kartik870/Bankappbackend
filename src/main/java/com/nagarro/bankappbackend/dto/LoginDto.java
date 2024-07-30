package com.nagarro.bankappbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto 
{
	@NotBlank                             //should not be empty
	private String password;
	
	@NotBlank                             //should not be empty
	@Email								  //Verifying this is email
	private String email;
}
