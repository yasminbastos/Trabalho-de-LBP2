package com.memorias.diario.controller;

import com.memorias.diario.models.Nota;
import com.memorias.diario.models.RegistroDia;
import com.memorias.diario.models.Usuario;
import com.memorias.diario.repositories.NotasRepository;
import com.memorias.diario.repositories.RegistroDiaRepository;
import com.memorias.diario.repositories.UsuariosRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Controller
public class CalendarioController {

    private final UsuariosRepository usuariosRepository;
    private final RegistroDiaRepository registroDiaRepository;
    private final NotasRepository notasRepository;

    public CalendarioController(UsuariosRepository usuariosRepository,
                                RegistroDiaRepository registroDiaRepository,
                                NotasRepository notasRepository) {
        this.usuariosRepository = usuariosRepository;
        this.registroDiaRepository = registroDiaRepository;
        this.notasRepository = notasRepository;
    }

    @GetMapping("/calendario")
    public String calendarioPage(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) return "redirect:/login";

        Usuario usuario = usuariosRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return "redirect:/login";

        var registros = registroDiaRepository.findAllByUsuarioOrderByDataDesc(usuario);
        List<Map<String, Object>> eventosRegistro = new ArrayList<>();
        for (RegistroDia r : registros) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("day", r.getData().getDayOfMonth());
            obj.put("month", r.getData().getMonthValue());
            obj.put("year", r.getData().getYear());

            Map<String, String> ev = new HashMap<>();
            ev.put("title", r.getHumor() != null ? r.getHumor() : "Sem humor");
            ev.put("time", r.getAnotacoes() != null ? r.getAnotacoes() : "");

            obj.put("events", List.of(ev));
            eventosRegistro.add(obj);
        }

        List<Nota> notas = notasRepository.findAllByUsuarioOrderByDataCriacaoDesc(usuario);
        List<Map<String, Object>> eventosNotas = new ArrayList<>();
        for (Nota n : notas) {
            LocalDate data = n.getDataCriacao().toLocalDate();

            Map<String, Object> obj = new HashMap<>();
            obj.put("day", data.getDayOfMonth());
            obj.put("month", data.getMonthValue());
            obj.put("year", data.getYear());

            Map<String, String> ev = new HashMap<>();
            ev.put("title", n.getTexto());
            String time = n.getDataCriacao().toLocalTime()
                    .withSecond(0).withNano(0).toString();
            ev.put("time", time);

            obj.put("events", List.of(ev));
            eventosNotas.add(obj);
        }

        for (RegistroDia r : registros) {
            LocalDate data = r.getData();

            Map<String, Object> obj = new HashMap<>();
            obj.put("day", data.getDayOfMonth());
            obj.put("month", data.getMonthValue());
            obj.put("year", data.getYear());

            Map<String, String> ev = new HashMap<>();
            ev.put("title", r.getHumor() != null ? r.getHumor() : "Sem humor");
            ev.put("time", r.getAnotacoes() != null ? r.getAnotacoes() : "");

            obj.put("events", List.of(ev));
            eventosNotas.add(obj);
        }

        model.addAttribute("nome", usuario.getNome());
        model.addAttribute("eventosRegistroDia", eventosRegistro); // para cores
        model.addAttribute("notasCalendario", eventosNotas);       // para lista da direita

        return "calendario";
    }

    @PostMapping("/calendario/nota")
    @ResponseBody
    public String salvarNotaCalendario(@RequestParam int day,
                                       @RequestParam int month,
                                       @RequestParam int year,
                                       @RequestParam String title,
                                       @RequestParam(required = false) String timeFrom,
                                       HttpSession session) {

        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) return "NOK";

        Usuario usuario = usuariosRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return "NOK";

        LocalTime hora = LocalTime.NOON;
        if (timeFrom != null && !timeFrom.isBlank() && timeFrom.matches("\\d{2}:\\d{2}")) {
            String[] partes = timeFrom.split(":");
            hora = LocalTime.of(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]));
        }

        LocalDate data = LocalDate.of(year, month, day);
        LocalDateTime dataCriacao = LocalDateTime.of(data, hora);

        Nota nota = new Nota();
        nota.setUsuario(usuario);
        nota.setTexto(title);
        nota.setDataCriacao(dataCriacao);

        notasRepository.save(nota);
        return "OK";
    }
}
