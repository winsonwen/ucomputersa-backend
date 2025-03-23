package com.ucomputersa.monolithic.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtModel {
    private BasicDataModel basicDataModel;

    private String jwtToken;

}
