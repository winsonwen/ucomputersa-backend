package com.ucomputersa.auth.domain.beans;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@ToString
public class CustomerRegisterRequest {

    @NotEmpty(message = "first name should be submitted")
    @Length( max=36,message = "first name should be not longer than 36")
    private String firstName;

    @NotEmpty(message = "last name should be submitted")
    @Length( max=36,message = "last name should be not longer than 36")
    private String lastName;

    @NotEmpty(message = "email should be submitted")
    @Email(message = "please enter a validate email")
    private String email;

    //TODO password strength
    @NotEmpty(message = "password should be submitted")
    private String password;
}
