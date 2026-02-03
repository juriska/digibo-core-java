package com.digibo.core.exception;

public class DatabaseException extends RuntimeException {

    private final String packageName;
    private final String procedureName;
    private final String errorCode;

    public DatabaseException(String message, String packageName, String procedureName) {
        super(message);
        this.packageName = packageName;
        this.procedureName = procedureName;
        this.errorCode = null;
    }

    public DatabaseException(String message, String packageName, String procedureName, String errorCode) {
        super(message);
        this.packageName = packageName;
        this.procedureName = procedureName;
        this.errorCode = errorCode;
    }

    public DatabaseException(String message, String packageName, String procedureName, Throwable cause) {
        super(message, cause);
        this.packageName = packageName;
        this.procedureName = procedureName;
        this.errorCode = extractErrorCode(cause);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    private String extractErrorCode(Throwable cause) {
        if (cause instanceof java.sql.SQLException sqlEx) {
            return String.valueOf(sqlEx.getErrorCode());
        }
        return null;
    }
}
