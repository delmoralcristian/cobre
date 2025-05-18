package com.delmoralcristian.notifier.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static void validateDates(LocalDateTime from, LocalDateTime to) {
        if (to.isBefore(from) || to.isEqual(from)) {
            throw new IllegalArgumentException("'to' datetime must be after 'from' datetime");
        }
    }

}
