package com.nagarro.bankappbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto 
{
	
	@NotBlank                             //should not be empty
	@Size(min = 6, max = 50)			  //defining size of pass
	private String password;
	
	@NotBlank                             //should not be empty
	@Email								  //Verifying this is email
	@Size(max = 50)						  //defining size of email
	private String email;
	
	@NotBlank                             //should not be empty
	@Size(min = 3, max = 25)			  //defining size of fullname
	private String firstName;
	
	@NotBlank                             //should not be empty
	@Size(min = 3, max = 25)			  //defining size of lastname
	private String lastName;
	
	@NotBlank 
	private String role;
	
}
