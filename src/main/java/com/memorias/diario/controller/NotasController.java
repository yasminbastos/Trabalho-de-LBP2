package com.memorias.diario.controller;

import com.memorias.diario.models.Nota;
import com.memorias.diario.models.Usuario;
import com.memorias.diario.repositories.NotasRepository;
import com.memorias.diario.repositories.UsuariosRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    public String salvarNota(@RequestParam("texto") String texto,
                             @RequestParam("horaInicio") String horaInicio,
                             @RequestParam("horaFim") String horaFim,
                             @RequestParam(value = "day", required = false) Integer day,
                             @RequestParam(value = "month", required = false) Integer month,
                             @RequestParam(value = "year", required = false) Integer year,
                             HttpSession session) {
        Usuario usuario = getUsuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        if (texto != null && !texto.trim().isEmpty()) {
            Nota nota = new Nota();
            nota.setUsuario(usuario);
            nota.setTexto(texto);

            LocalDate data;
            if (day != null && month != null && year != null) {
                data = LocalDate.of(year, month, day);
            } else {
                data = LocalDate.now();
            }

            LocalTime inicio = parseHora(horaInicio);
            LocalTime fim = parseHora(horaFim);

            nota.setHoraInicio(inicio);
            nota.setHoraFim(fim);
            nota.setDataCriacao(LocalDateTime.of(data, inicio));

            notasRepository.save(nota);
        }
        return "redirect:/notas";
    }

    @PostMapping("/notas/{id}/excluir")
    public String excluirNota(@PathVariable Long id, HttpSession session) {
        Usuario usuario = getUsuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        notasRepository.findById(id).ifPresent(n -> {
            if (n.getUsuario().getId() == usuario.getId()) {
                notasRepository.delete(n);
            }
        });

        return "redirect:/notas";
    }



    private LocalTime parseHora(String valor) {
        if (valor != null && !valor.isBlank() && valor.matches("\\d{2}:\\d{2}")) {
            String[] partes = valor.split(":");
            return LocalTime.of(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]));
        }
        return LocalTime.NOON;
    }
}
