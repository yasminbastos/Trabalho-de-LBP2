package com.memorias.diario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PerfilController {

    @GetMapping("/perfil")
    public String mostrarPerfil(Model model) {
        // Adiciona os atributos que o Thymeleaf vai renderizar
        model.addAttribute("nome", "Mayara Cardoso");
        model.addAttribute("email", "maymay.cardoso@example.com");

        // Retorna o nome da página HTML (perfil.html dentro de /templates)
        return "perfil";
    }
}
