package com.ucomputersa.auth.domain.beans;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@ToString
public class MemberRegisterRequest {

    @NotEmpty(message = "first name should be submitted")
    @Length( max=36,message = "first name should be not longer than 36")
    private String firstName;
    @NotEmpty(message = "last name should be submitted")
    @Length( max=36,message = "last name should be not longer than 36")
    private String lastName;

    @NotEmpty(message = "last name should be submitted")
    @Past( message = "Date of birth should be in the past")
    private LocalDate dateOfBirth;

    @NotEmpty(message = "email should be submitted")
    @Email(message = "please enter a validate email")
    private String email;
    @NotEmpty(message = "phone number should  be submitted")
    @Pattern(regexp = "^\\d{10}$", message = "The format of phone Number isn't correct")
    private String phone;
    @NotEmpty(message = "password should be submitted")
    private String password;
    @NotNull(message = "address should be submitted")
    private AddressBean address;
}
