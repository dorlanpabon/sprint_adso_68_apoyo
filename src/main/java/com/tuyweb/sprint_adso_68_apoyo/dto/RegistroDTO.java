package com.tuyweb.sprint_adso_68_apoyo.dto;

import com.tuyweb.sprint_adso_68_apoyo.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistroDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email no válido")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "Mínimo 6 caracteres")
    private String password;

    @NotBlank(message = "Confirma la contraseña")
    private String confirmPassword;

    /**
     * Rol elegido en el registro. Por defecto CLIENTE.
     * El formulario solo ofrece CLIENTE y VENDEDOR; ADMIN se crea por seeder o CRUD.
     */
    private Rol rol = Rol.CLIENTE;
}
