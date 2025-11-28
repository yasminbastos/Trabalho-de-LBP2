package com.memorias.diario.controller;

import com.memorias.diario.service.NoraService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/nora")
public class NoraController {

    private final NoraService noraService;

    public NoraController(NoraService noraService) {
        this.noraService = noraService;
    }

    @GetMapping
    public String paginaNora(HttpSession session, Model model) {

        String humor = (String) session.getAttribute("humorHoje");
        model.addAttribute("humor", humor);

        return "nora";
    }

    @PostMapping
    public String iniciarSugestoes(HttpSession session, Model model) {

        String humor = (String) session.getAttribute("humorHoje");

        Map<Integer, List<String>> respostas =
                (Map<Integer, List<String>>) session.getAttribute("respostas");

        // Garantia de segurança contra null
        if (respostas == null) {
            respostas = Map.of();
        }

        List<String> sugestoes = noraService.gerarSugestoes(humor, respostas);

        model.addAttribute("humor", humor);
        model.addAttribute("sugestoes", sugestoes);

        return "nora";
    }
}
