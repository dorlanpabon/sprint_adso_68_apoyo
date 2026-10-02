package com.tuyweb.sprint_adso_68_apoyo.model;

/**
 * Roles de la aplicación. Se persisten como String (CLIENTE, VENDEDOR, ADMIN)
 * y Spring Security los usa como ROLE_CLIENTE, etc.
 */
public enum Rol {
    CLIENTE,
    VENDEDOR,
    ADMIN;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
