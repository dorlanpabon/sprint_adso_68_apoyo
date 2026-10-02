package com.tuyweb.sprint_adso_68_apoyo.dto;

import com.tuyweb.sprint_adso_68_apoyo.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioFormDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email no válido")
    @Size(max = 150)
    private String email;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol = Rol.CLIENTE;

    private boolean activo = true;

    /**
     * Solo se usa al crear. Al editar, si viene vacío se conserva la actual.
     */
    @Size(min = 6, max = 100, message = "Mínimo 6 caracteres")
    private String password;
}
