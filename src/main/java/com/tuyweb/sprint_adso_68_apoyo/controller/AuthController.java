package com.tuyweb.sprint_adso_68_apoyo.controller;

import com.tuyweb.sprint_adso_68_apoyo.dto.RegistroDTO;
import com.tuyweb.sprint_adso_68_apoyo.model.Rol;
import com.tuyweb.sprint_adso_68_apoyo.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        if (!model.containsAttribute("registroDTO")) {
            RegistroDTO dto = new RegistroDTO();
            dto.setRol(Rol.CLIENTE);
            model.addAttribute("registroDTO", dto);
        }
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registroDTO") RegistroDTO dto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registro";
        }
        try {
            usuarioService.registrar(dto);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "registro.duplicado", e.getMessage());
            return "registro";
        }
        redirectAttributes.addFlashAttribute("registrado", true);
        return "redirect:/login?registrado";
    }
}
