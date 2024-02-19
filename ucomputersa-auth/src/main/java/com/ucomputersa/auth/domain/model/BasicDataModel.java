package com.ucomputersa.auth.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicDataModel {

   public String email;

    public  String lastName;

    public   String firstName;
}
