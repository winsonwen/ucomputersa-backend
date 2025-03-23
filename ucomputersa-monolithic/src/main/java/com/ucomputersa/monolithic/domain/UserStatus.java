package com.ucomputersa.monolithic.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum UserStatus {
    ACTIVE,
    INACTIVE,
    BLOCKED;
}
