package com.memorias.diario.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/formulario")
@SessionAttributes("respostas")
public class FormController {

    public static class Pergunta {
        private String texto;
        private List<String> opcoes;
        private boolean temCampoOutro;

        public Pergunta(String texto, List<String> opcoes, boolean temCampoOutro) {
            this.texto = texto;
            this.opcoes = opcoes;
            this.temCampoOutro = temCampoOutro;
        }

        public String getTexto() { return texto; }
        public List<String> getOpcoes() { return opcoes; }
        public boolean isTemCampoOutro() { return temCampoOutro; }
    }

    private final List<Pergunta> perguntas = List.of(
            new Pergunta("O que costuma te deixar mais animado?",
                    List.of("Ouvir música", "Sair para caminhar", "Conversar com amigos",
                            "Assistir filmes/séries", "Praticar hobbies"), true),
            new Pergunta("Você costuma praticar atividade física?",
                    List.of("Sim, regularmente", "Às vezes", "Raramente / nunca"), false),
            new Pergunta("Como você costuma lidar com dias ruins?",
                    List.of("Tento relaxar", "Converso com alguém", "Fico sozinho(a)",
                            "Como algo gostoso", "Ouço música"), false),
            new Pergunta("Você gosta de ouvir música quando está triste?",
                    List.of("Sim", "Não"), false),
            new Pergunta("Qual tipo de música você mais ouve?",
                    List.of("Pop", "Rock", "Sertanejo", "Lo-Fi", "Clássica"), true),
            new Pergunta("Quer que o sistema recomende playlists conforme seu humor?",
                    List.of("Sim", "Não"), false),
            new Pergunta("Você dorme bem em média?",
                    List.of("Sim", "Às vezes", "Não muito"), false),
            new Pergunta("Você costuma anotar o que sente?",
                    List.of("Sim, em diário", "Não"), false),
            new Pergunta("Você gostaria que o sistema lembrasse de registrar seu humor diariamente?",
                    List.of("Sim", "Não"), false)
    );

    @ModelAttribute("respostas")
    public Map<Integer, List<String>> respostas() {
        return new HashMap<>();
    }

    @GetMapping
    public String mostrarIntro(HttpSession session) {

        Object usuario = session.getAttribute("usuario");
        Object primeiroAcesso = session.getAttribute("primeiroAcesso");

        if (usuario == null && primeiroAcesso == null) {
            return "401"; // acesso negado
        }

        return "formularioIntro";
    }

    @GetMapping("/pergunta/{indice}")
    public String mostrarPergunta(@PathVariable int indice,
                                  Model model,
                                  HttpSession session,
                                  @ModelAttribute("respostas") Map<Integer, List<String>> respostas) {

        Object usuario = session.getAttribute("usuario");
        Object primeiroAcesso = session.getAttribute("primeiroAcesso");

        if (usuario == null && primeiroAcesso == null) {
            return "401";
        }

        if (indice < 0 || indice >= perguntas.size()) {
            return "redirect:/formulario/fim";
        }

        Pergunta perguntaAtual = perguntas.get(indice);
        List<String> respAnterior = respostas.getOrDefault(indice, new ArrayList<>());

        model.addAttribute("indice", indice);
        model.addAttribute("pergunta", perguntaAtual);
        model.addAttribute("respostaAnterior", respAnterior);
        model.addAttribute("ultimaPagina", indice == perguntas.size() - 1);

        return "perguntasFormulario";
    }

    @PostMapping("/pergunta/{indice}")
    public String salvarResposta(@PathVariable int indice,
                                 @RequestParam(required = false) String[] resposta,
                                 @RequestParam(required = false) String outro,
                                 @ModelAttribute("respostas") Map<Integer, List<String>> respostas,
                                 Model model) {

        List<String> listaRespostas = new ArrayList<>();

        if (resposta != null) listaRespostas.addAll(Arrays.asList(resposta));
        if (outro != null && !outro.isBlank()) listaRespostas.add(outro);

        if (listaRespostas.isEmpty()) {
            model.addAttribute("aviso", "💡 Preencha pelo menos um campo antes de continuar!");
            model.addAttribute("indice", indice);
            model.addAttribute("pergunta", perguntas.get(indice));
            model.addAttribute("ultimaPagina", indice == perguntas.size() - 1);
            model.addAttribute("respostaAnterior", respostas.getOrDefault(indice, new ArrayList<>()));
            return "perguntasFormulario";
        }

        respostas.put(indice, listaRespostas);

        if (indice + 1 < perguntas.size()) {
            return "redirect:/formulario/pergunta/" + (indice + 1);
        } else {
            return "redirect:/formulario/fim";
        }
    }

    @GetMapping("/fim")
    public String mostrarFim(@ModelAttribute("respostas") Map<Integer, List<String>> respostas,
                             Model model,
                             HttpSession session) {

        Object usuario = session.getAttribute("usuario");
        Object primeiroAcesso = session.getAttribute("primeiroAcesso");

        if (usuario == null && primeiroAcesso == null) {
            return "401";
        }

        session.removeAttribute("primeiroAcesso");

        model.addAttribute("respostas", respostas);
        return "fim-PerguntasFormulario";
    }
}
