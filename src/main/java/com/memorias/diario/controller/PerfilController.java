package com.memorias.diario.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PerfilController {

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        // Pega os dados salvos na sessão no momento do login
        String usuario = (String) session.getAttribute("usuario");
        String email = (String) session.getAttribute("email");

        if (usuario == null || email == null) {
            return "401";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("email", email);

        return "perfil";
    }
}
