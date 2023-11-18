package com.ucomputersa.auth.domain.model;

import lombok.Data;

@Data
public class OAuth2Model {

    private String access_token;

    private String expires_in;

    private String scope;

    private String token_type;
    private String id_token;

}
