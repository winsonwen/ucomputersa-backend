package com.ucomputersa.auth.domain.beans;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomerLoginRequest {
    @NotEmpty(message = "email should be submitted")
    @Email(message = "please enter a validate username")
    private String email;

    @NotEmpty(message = "password should be submitted")
    private String password;

}
