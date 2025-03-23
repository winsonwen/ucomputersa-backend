package com.ucomputersa.auth.domain.model;

import com.ucomputersa.common.constant.RoleEnum;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BasicDataModel {

   public String email;

    public  String lastName;

    public   String firstName;
}
