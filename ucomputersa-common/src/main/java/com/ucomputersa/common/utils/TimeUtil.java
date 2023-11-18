package com.ucomputersa.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtil {

    private static final ZoneId ZONE_ID = ZoneId.of("US/Eastern");


    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(ZONE_ID);
    }

    public static Date getCurrentDateTime() {
        return Date.from(getCurrentLocalDateTime().atZone(ZONE_ID).toInstant());
    }

    public static Date getCurrentDateTimePlusHours(int hours) {
        return Date.from(getCurrentLocalDateTime().plusHours(hours).atZone(ZONE_ID).toInstant());
    }
}
