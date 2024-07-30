package com.nagarro.bankappbackend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    private int age;
    private String profilePhoto; // This will be a base64 encoded string
    private boolean valid;
}