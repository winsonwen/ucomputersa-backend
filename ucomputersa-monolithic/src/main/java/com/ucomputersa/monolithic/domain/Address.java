package com.ucomputersa.monolithic.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Address {
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
}
