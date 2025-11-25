package com.memorias.diario.controller;

import com.memorias.diario.models.Nota;
import com.memorias.diario.models.Usuario;
import com.memorias.diario.repositories.NotasRepository;
import com.memorias.diario.repositories.UsuariosRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class NotasController {

    private final NotasRepository notasRepository;
    private final UsuariosRepository usuariosRepository;

    public NotasController(NotasRepository notasRepository,
                           UsuariosRepository usuariosRepository) {
        this.notasRepository = notasRepository;
        this.usuariosRepository = usuariosRepository;
    }

    private Usuario getUsuarioLogado(HttpSession session) {
        Long id = (Long) session.getAttribute("usuarioId");
        if (id == null) return null;
        return usuariosRepository.findById(id).orElse(null);
    }

    @GetMapping("/notas")
    public String mostrarNotas(Model model, HttpSession session) {
        Usuario usuario = getUsuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("notas", notasRepository.findAllByUsuarioOrderByDataCriacaoDesc(usuario));
        return "notas";
    }

    @PostMapping("/notas")
    public String salvarNota(@RequestParam("texto") String texto, HttpSession session) {
        Usuario usuario = getUsuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        if (texto != null && !texto.trim().isEmpty()) {
            Nota nota = new Nota();
            nota.setUsuario(usuario);
            nota.setTexto(texto);
            nota.setDataCriacao(LocalDateTime.now());
            notasRepository.save(nota);
        }
        return "redirect:/notas";
    }
}
