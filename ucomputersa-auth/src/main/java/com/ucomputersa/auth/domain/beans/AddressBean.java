package com.ucomputersa.auth.domain.beans;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@ToString
public class AddressBean {
    @NotEmpty(message = "street address should be submitted")
    @Length(max = 88, message = "street address should be not longer than 88")
    private String streetAddress;

    @Length(max = 48, message = "last name should be not longer than 48")
    private String additionalInfo;

    @NotEmpty(message = "city should be submitted")
    @Length(max = 36, message = "last name should be not longer than 36")
    private String city;

    @NotEmpty(message = "state should be submitted")
    @Length(max = 24, message = "last name should be not longer than 24")
    private String state;

    @NotEmpty(message = "zipcode should be submitted")
    @Pattern(regexp = "^\\d{5}$", message = "The format of zipcode isn't correct")
    private String zipcode;
}
