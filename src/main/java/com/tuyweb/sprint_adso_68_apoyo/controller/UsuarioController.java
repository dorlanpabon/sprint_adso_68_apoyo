package com.tuyweb.sprint_adso_68_apoyo.controller;

import com.tuyweb.sprint_adso_68_apoyo.dto.UsuarioDTO;
import com.tuyweb.sprint_adso_68_apoyo.dto.UsuarioFormDTO;
import com.tuyweb.sprint_adso_68_apoyo.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public String listar(Model model) {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String nuevo(Model model) {
        if (!model.containsAttribute("usuarioForm")) {
            UsuarioFormDTO form = new UsuarioFormDTO();
            form.setActivo(true);
            model.addAttribute("usuarioForm", form);
        }
        model.addAttribute("esEdicion", false);
        return "usuarios/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String crear(@Valid @ModelAttribute("usuarioForm") UsuarioFormDTO form,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("esEdicion", false);
            return "usuarios/form";
        }
        try {
            usuarioService.crearDesdeForm(form);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "usuario.duplicado", e.getMessage());
            model.addAttribute("esEdicion", false);
            return "usuarios/form";
        }
        redirectAttributes.addFlashAttribute("mensaje", "Usuario creado correctamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuarioForm", usuarioService.buscarFormPorId(id));
        model.addAttribute("esEdicion", true);
        return "usuarios/form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("usuarioForm") UsuarioFormDTO form,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("esEdicion", true);
            return "usuarios/form";
        }
        try {
            usuarioService.actualizarDesdeForm(id, form);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "usuario.duplicado", e.getMessage());
            model.addAttribute("esEdicion", true);
            return "usuarios/form";
        }
        redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado correctamente");
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
        return "redirect:/usuarios";
    }
}
