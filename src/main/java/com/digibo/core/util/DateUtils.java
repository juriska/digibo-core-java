package com.digibo.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for date operations
 */
public final class DateUtils {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {
        // Utility class - prevent instantiation
    }

    public static Date parseDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        try {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isBlank()) {
            return null;
        }
        try {
            return new SimpleDateFormat(DEFAULT_DATETIME_FORMAT).parse(dateTimeString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
    }

    public static String formatDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(DEFAULT_DATETIME_FORMAT).format(date);
    }
}
