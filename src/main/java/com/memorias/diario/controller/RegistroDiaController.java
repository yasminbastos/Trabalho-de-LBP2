package com.memorias.diario.controller;

import com.memorias.diario.models.RegistroDia;
import com.memorias.diario.models.Usuario;
import com.memorias.diario.repositories.RegistroDiaRepository;
import com.memorias.diario.repositories.UsuariosRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class RegistroDiaController {

    private final RegistroDiaRepository registroDiaRepository;
    private final UsuariosRepository usuariosRepository;

    public RegistroDiaController(RegistroDiaRepository registroDiaRepository,
                                 UsuariosRepository usuariosRepository) {
        this.registroDiaRepository = registroDiaRepository;
        this.usuariosRepository = usuariosRepository;
    }

    private Usuario getUsuarioLogado(HttpSession session) {
        Long id = (Long) session.getAttribute("usuarioId");
        if (id == null) return null;
        return usuariosRepository.findById(id).orElse(null);
    }

    @GetMapping("/registro")
    public String registroPage(HttpSession session, Model model) {
        Usuario usuario = getUsuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        LocalDate hoje = LocalDate.now();

        RegistroDia registro = registroDiaRepository
                .findByUsuarioAndData(usuario, hoje)
                .orElseGet(() -> {
                    RegistroDia r = new RegistroDia();
                    r.setUsuario(usuario);
                    r.setData(hoje);
                    return r;
                });

        model.addAttribute("nome", usuario.getNome());
        model.addAttribute("registro", registro);
        return "registro";
    }

    @PostMapping("/registro")
    public String salvarRegistro(@RequestParam String humor,
                                 @RequestParam(name = "texto_humor", required = false) String anotacoes,
                                 HttpSession session,
                                 Model model) {
        Usuario usuario = getUsuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        LocalDate hoje = LocalDate.now();

        RegistroDia registro = registroDiaRepository
                .findByUsuarioAndData(usuario, hoje)
                .orElseGet(() -> {
                    RegistroDia r = new RegistroDia();
                    r.setUsuario(usuario);
                    r.setData(hoje);
                    return r;
                });

        registro.setHumor(humor);
        registro.setAnotacoes(anotacoes);
        registroDiaRepository.save(registro);

        // Salva o humor do dia na session para a Nora usar depois
        session.setAttribute("humorHoje", humor);

        return "redirect:/calendario";

    }


}
