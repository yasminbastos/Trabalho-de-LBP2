package com.memorias.diario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NotasController {

    // Lista em memória para registrar as notas
    private List<String> notas = new ArrayList<>();

    @GetMapping("/notas")
    public String mostrarNotas(Model model) {
        model.addAttribute("notas", notas);
        return "notas";
    }

    @PostMapping("/notas")
    public String salvarNota(@RequestParam("texto") String texto) {
        if (texto != null && !texto.trim().isEmpty()) {
            notas.add(texto);
        }
        return "redirect:/notas";
    }
}
