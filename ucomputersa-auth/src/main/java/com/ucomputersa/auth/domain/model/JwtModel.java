package com.ucomputersa.auth.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtModel {
  public   BasicDataModel basicDataModel;

    public  String jwtToken;

}
