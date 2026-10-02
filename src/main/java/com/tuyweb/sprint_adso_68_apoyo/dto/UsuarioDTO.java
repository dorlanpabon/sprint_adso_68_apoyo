package com.tuyweb.sprint_adso_68_apoyo.dto;

import com.tuyweb.sprint_adso_68_apoyo.model.Rol;

/**
 * Solo lectura (listados, detalle). Sin password.
 */
public record UsuarioDTO(
        Long id,
        String nombre,
        String email,
        Rol rol,
        boolean activo
) {
}
