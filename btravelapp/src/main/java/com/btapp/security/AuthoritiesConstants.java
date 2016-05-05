package com.btapp.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    
    // modificat 14.03.2016
    public static final String MANAGER = "ROLE_MANAGER";
    
    public static final String SUPPLIER = "ROLE_SUPPLIER";

    private AuthoritiesConstants() {
    }
}
