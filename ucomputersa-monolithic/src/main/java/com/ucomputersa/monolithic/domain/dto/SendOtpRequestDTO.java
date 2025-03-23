package com.ucomputersa.monolithic.domain.dto;

import lombok.Data;

@Data

public class SendOtpRequestDTO {
    String phoneNumber;
    String countryCode;
}
