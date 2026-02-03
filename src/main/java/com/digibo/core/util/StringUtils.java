package com.digibo.core.util;

/**
 * Utility class for string operations
 */
public final class StringUtils {

    private StringUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Convert snake_case to camelCase
     */
    public static String snakeToCamel(String snakeCase) {
        if (snakeCase == null || snakeCase.isBlank()) {
            return snakeCase;
        }

        StringBuilder result = new StringBuilder();
        boolean nextUpperCase = false;

        for (char c : snakeCase.toLowerCase().toCharArray()) {
            if (c == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    result.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    result.append(c);
                }
            }
        }

        return result.toString();
    }

    /**
     * Convert camelCase to snake_case
     */
    public static String camelToSnake(String camelCase) {
        if (camelCase == null || camelCase.isBlank()) {
            return camelCase;
        }

        StringBuilder result = new StringBuilder();

        for (char c : camelCase.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append('_').append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Check if a string is null or blank
     */
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    /**
     * Check if a string is not null and not blank
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Return default value if string is blank
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }
}
