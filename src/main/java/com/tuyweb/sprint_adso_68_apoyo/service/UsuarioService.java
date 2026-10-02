package com.tuyweb.sprint_adso_68_apoyo.service;

import com.tuyweb.sprint_adso_68_apoyo.dto.RegistroDTO;
import com.tuyweb.sprint_adso_68_apoyo.dto.UsuarioDTO;
import com.tuyweb.sprint_adso_68_apoyo.dto.UsuarioFormDTO;
import com.tuyweb.sprint_adso_68_apoyo.mapper.UsuarioMapper;
import com.tuyweb.sprint_adso_68_apoyo.model.Rol;
import com.tuyweb.sprint_adso_68_apoyo.model.Usuario;
import com.tuyweb.sprint_adso_68_apoyo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    // ---------- Registro público ----------

    public UsuarioDTO registrar(RegistroDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        // Seguridad: desde el formulario público no se permite crear ADMIN directamente
        Rol rol = dto.getRol() == Rol.ADMIN ? Rol.CLIENTE : (dto.getRol() == null ? Rol.CLIENTE : dto.getRol());

        Usuario usuario = usuarioMapper.fromRegistro(dto);
        usuario.setRol(rol);
        usuario.setActivo(true);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    // ---------- CRUD (admin) ----------

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioFormDTO buscarFormPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        return usuarioMapper.toFormDTO(usuario);
    }

    public UsuarioDTO crearDesdeForm(UsuarioFormDTO form) {
        if (usuarioRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria al crear");
        }
        Usuario usuario = usuarioMapper.fromForm(form);
        usuario.setPassword(passwordEncoder.encode(form.getPassword()));
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    public UsuarioDTO actualizarDesdeForm(Long id, UsuarioFormDTO form) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));

        // Si cambió el email, validar que no exista
        if (!usuario.getEmail().equalsIgnoreCase(form.getEmail())
                && usuarioRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        usuarioMapper.updateEntityFromForm(form, usuario);

        // Solo se cambia la contraseña si se digitó una nueva
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(form.getPassword()));
        }
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
