package com.ucomputersa.monolithic.domain.model;

import com.ucomputersa.monolithic.constant.RoleEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BasicDataModel {

    private String email;
    private String phoneNumber;
    private String countryCode;

    private String lastName;

    private String firstName;

    private List<RoleEnum> roles;


}
