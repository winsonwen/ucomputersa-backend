package com.ucomputersa.monolithic.domain.dto;


import lombok.Data;

@Data
public class LoginRequestDTO {


    String password;
    String phoneNumber;
    String countryCode;
    String opt;

}
