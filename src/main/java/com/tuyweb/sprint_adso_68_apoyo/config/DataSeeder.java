package com.tuyweb.sprint_adso_68_apoyo.config;

import com.tuyweb.sprint_adso_68_apoyo.model.Rol;
import com.tuyweb.sprint_adso_68_apoyo.model.Usuario;
import com.tuyweb.sprint_adso_68_apoyo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea usuarios de prueba si la tabla está vacía.
 * Cambia las claves en producción.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        crearSiNoExiste("Administrador", "admin@tuyweb.com", "admin123", Rol.ADMIN);
        crearSiNoExiste("Vendedor Demo", "vendedor@tuyweb.com", "vendedor123", Rol.VENDEDOR);
        crearSiNoExiste("Cliente Demo", "cliente@tuyweb.com", "cliente123", Rol.CLIENTE);
    }

    private void crearSiNoExiste(String nombre, String email, String rawPassword, Rol rol) {
        if (usuarioRepository.existsByEmail(email)) {
            return;
        }
        crear(nombre, email, rawPassword, rol);
    }

    private void crear(String nombre, String email, String rawPassword, Rol rol) {
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRol(rol);
        u.setActivo(true);
        usuarioRepository.save(u);
    }
}
