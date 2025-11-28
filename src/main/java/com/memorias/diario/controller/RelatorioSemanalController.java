package com.memorias.diario.controller;

import com.memorias.diario.models.RegistroDia;
import com.memorias.diario.models.Usuario;
import com.memorias.diario.repositories.RegistroDiaRepository;
import com.memorias.diario.repositories.UsuariosRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/relatorio")
public class RelatorioSemanalController {

    private final UsuariosRepository usuariosRepository;
    private final RegistroDiaRepository registroDiaRepository;

    public RelatorioSemanalController(UsuariosRepository usuariosRepository,
                                      RegistroDiaRepository registroDiaRepository) {
        this.usuariosRepository = usuariosRepository;
        this.registroDiaRepository = registroDiaRepository;
    }

    @GetMapping
    public String relatorioSemana(HttpSession session, Model model) {

        Long id = (Long) session.getAttribute("usuarioId");
        if (id == null) return "redirect:/login";

        Usuario usuario = usuariosRepository.findById(id).orElse(null);
        if (usuario == null) return "redirect:/login";

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.minusDays(6);

        List<RegistroDia> semana = registroDiaRepository
                .findAllByUsuarioAndDataBetweenOrderByDataDesc(usuario, inicio, hoje);

        model.addAttribute("semana", semana);
        return "relatorioSemanal";
    }
}
