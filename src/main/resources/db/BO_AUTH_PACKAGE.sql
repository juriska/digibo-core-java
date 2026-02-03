-- ============================================================================
-- BO_AUTH Package - User Permission Management
-- ============================================================================
-- This package provides user permission information to the Spring Boot backend.
-- It queries Oracle's data dictionary to determine which PL/SQL procedures
-- a user has EXECUTE grants on.
--
-- REQUIRED: Create this package in Oracle and grant EXECUTE to the app user.
-- ============================================================================

CREATE OR REPLACE PACKAGE BO_AUTH AS

    /**
     * Get all permissions for a user based on Oracle grants.
     * Returns a cursor with PACKAGE_NAME and PROCEDURE_NAME columns.
     *
     * @param P_USERNAME  The Oracle username to check permissions for
     * @param P_CURSOR    Output cursor with permission results
     */
    PROCEDURE GET_USER_PERMISSIONS(
        P_USERNAME  IN  VARCHAR2,
        P_CURSOR    OUT SYS_REFCURSOR
    );

    /**
     * Check if a specific user has permission to execute a procedure.
     *
     * @param P_USERNAME      The Oracle username
     * @param P_PACKAGE_NAME  The package name (e.g., 'BOCUSTOMER')
     * @param P_PROCEDURE     The procedure name (e.g., 'FIND')
     * @return 1 if user has permission, 0 otherwise
     */
    FUNCTION HAS_PERMISSION(
        P_USERNAME      IN VARCHAR2,
        P_PACKAGE_NAME  IN VARCHAR2,
        P_PROCEDURE     IN VARCHAR2
    ) RETURN NUMBER;

END BO_AUTH;
/

CREATE OR REPLACE PACKAGE BODY BO_AUTH AS

    -- Define which packages are application packages (not system packages)
    -- This filters out Oracle system packages from the permission list
    c_app_package_prefix CONSTANT VARCHAR2(10) := 'BO%';

    PROCEDURE GET_USER_PERMISSIONS(
        P_USERNAME  IN  VARCHAR2,
        P_CURSOR    OUT SYS_REFCURSOR
    ) AS
    BEGIN
        -- Query Oracle's data dictionary to find all packages the user can execute
        -- This includes direct grants and grants through roles
        OPEN P_CURSOR FOR
            SELECT DISTINCT
                atp.table_name AS PACKAGE_NAME,
                ap.procedure_name AS PROCEDURE_NAME
            FROM all_tab_privs atp
            JOIN all_procedures ap ON atp.table_name = ap.object_name
            WHERE atp.grantee = UPPER(P_USERNAME)
              AND atp.privilege = 'EXECUTE'
              AND atp.table_name LIKE c_app_package_prefix
              AND ap.procedure_name IS NOT NULL
            UNION
            -- Also include grants through roles
            SELECT DISTINCT
                atp.table_name AS PACKAGE_NAME,
                ap.procedure_name AS PROCEDURE_NAME
            FROM all_tab_privs atp
            JOIN all_procedures ap ON atp.table_name = ap.object_name
            JOIN dba_role_privs drp ON drp.granted_role = atp.grantee
            WHERE drp.grantee = UPPER(P_USERNAME)
              AND atp.privilege = 'EXECUTE'
              AND atp.table_name LIKE c_app_package_prefix
              AND ap.procedure_name IS NOT NULL
            ORDER BY PACKAGE_NAME, PROCEDURE_NAME;
    END GET_USER_PERMISSIONS;

    FUNCTION HAS_PERMISSION(
        P_USERNAME      IN VARCHAR2,
        P_PACKAGE_NAME  IN VARCHAR2,
        P_PROCEDURE     IN VARCHAR2
    ) RETURN NUMBER AS
        v_count NUMBER := 0;
    BEGIN
        -- Check direct grants
        SELECT COUNT(*) INTO v_count
        FROM all_tab_privs atp
        JOIN all_procedures ap ON atp.table_name = ap.object_name
        WHERE atp.grantee = UPPER(P_USERNAME)
          AND atp.privilege = 'EXECUTE'
          AND atp.table_name = UPPER(P_PACKAGE_NAME)
          AND ap.procedure_name = UPPER(P_PROCEDURE);

        IF v_count > 0 THEN
            RETURN 1;
        END IF;

        -- Check grants through roles
        SELECT COUNT(*) INTO v_count
        FROM all_tab_privs atp
        JOIN all_procedures ap ON atp.table_name = ap.object_name
        JOIN dba_role_privs drp ON drp.granted_role = atp.grantee
        WHERE drp.grantee = UPPER(P_USERNAME)
          AND atp.privilege = 'EXECUTE'
          AND atp.table_name = UPPER(P_PACKAGE_NAME)
          AND ap.procedure_name = UPPER(P_PROCEDURE);

        IF v_count > 0 THEN
            RETURN 1;
        END IF;

        RETURN 0;
    END HAS_PERMISSION;

END BO_AUTH;
/

-- Grant execute on BO_AUTH to the application service account
-- GRANT EXECUTE ON BO_AUTH TO app_service_account;

-- ============================================================================
-- Alternative: Custom Permission Table Approach
-- ============================================================================
-- If you prefer to manage permissions separately from Oracle grants,
-- you can create a custom permission table:
--
-- CREATE TABLE APP_USER_PERMISSIONS (
--     ID              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--     USERNAME        VARCHAR2(100) NOT NULL,
--     PACKAGE_NAME    VARCHAR2(100) NOT NULL,
--     PROCEDURE_NAME  VARCHAR2(100) NOT NULL,
--     GRANTED_DATE    DATE DEFAULT SYSDATE,
--     GRANTED_BY      VARCHAR2(100),
--     CONSTRAINT UK_USER_PERM UNIQUE (USERNAME, PACKAGE_NAME, PROCEDURE_NAME)
-- );
--
-- Then modify GET_USER_PERMISSIONS to query this table instead.
-- ============================================================================
