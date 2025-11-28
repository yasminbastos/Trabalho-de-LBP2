package com.memorias.diario.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;


@Controller
@RequestMapping("/formulario") //todas as rotas do controller começam dentro desta rota
@SessionAttributes("respostas") //guarda as respostas do usuário enquanto ele navega pelas perguntas
public class FormController {


    // Componentes dos formulários (texto, opcão, outros)
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


    // Lista de perguntas do formulário (Parte para implementar no Banco de Dados - Gabyzinha)
    private final List<Pergunta> perguntas = List.of(
            new Pergunta("O que costuma te deixar mais animado?",
                    List.of("Ouvir música", "Sair para caminhar", "Conversar com amigos",
                            "Assistir filmes/séries", "Praticar hobbies"), true),
            new Pergunta("Você costuma praticar atividade física?",
                    List.of("Sim, regularmente", "Às vezes", "Raramente / nunca"), false),
            new Pergunta("Você dorme bem em média?",
                    List.of("Sim", "Às vezes", "Não muito"), false),
            new Pergunta("Você costuma anotar o que sente?",
                    List.of("Sim, em diário", "Não"), false),
            new Pergunta("Você gostaria que o sistema lembrasse de registrar seu humor diariamente?",
                    List.of("Sim", "Não"), false)
    );

    // Toda vez que o formulário é acessado o mapa "resposta" é acionado
    @ModelAttribute("respostas")
    public Map<Integer, List<String>> respostas() {  //Guarda cada resposta enquanto o usuário navega pelo formulário
        return new HashMap<>();
    }


    //Página inicial
    @GetMapping
    public String mostrarIntro(HttpSession session) {
        if (session.getAttribute("usuario") == null){  //verifica se algúem acessou a rota "formulario" sem estar logado
            return "401";
        }
        return "formularioIntro";
    }


    //Mostrar pergunta
    @GetMapping("/pergunta/{indice}") // recebe o índice da pergunta via URL(/pergunta/0,/pergunta1 etc
    public String mostrarPergunta(@PathVariable int indice, Model model, HttpSession session,
                                  @ModelAttribute("respostas") Map<Integer, List<String>> respostas) {

       if (session.getAttribute("usuario") == null){
           return "401";
       }
        if (indice < 0 || indice >= perguntas.size()) { //verificação para checar se o índice é válido, se não vai para o fim
            return "redirect:/formulario/fim";
        }


        //Pega a pergunta atual da lista e depois as respostas anteriores para preencher os "outros" caso houver
        Pergunta perguntaAtual = perguntas.get(indice);
        List<String> respAnterior = respostas.getOrDefault(indice, new ArrayList<>());


        model.addAttribute("indice", indice);
        model.addAttribute("pergunta", perguntaAtual);
        model.addAttribute("respostaAnterior", respAnterior);
        model.addAttribute("ultimaPagina", indice == perguntas.size() - 1);


        return "perguntasFormulario";
    }


    // Salvar respostas
    @PostMapping("/pergunta/{indice}")
    public String salvarResposta(@PathVariable int indice,
                                 @RequestParam(required = false) String[] resposta,
                                 @RequestParam(required = false) String outro,
                                 @ModelAttribute("respostas") Map<Integer, List<String>> respostas,
                                 Model model) {


        // Há um campo de verificação caso o usuário não preencha nenhum campo
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


        respostas.put(indice, listaRespostas); //Caso haja respostas é aqui que salva no mapa




        // Paginação, cada pergunta respondida renderiza outra pergunta até chegar ao fim
        if (indice + 1 < perguntas.size()) {
            return "redirect:/formulario/pergunta/" + (indice + 1);
        } else {
            return "redirect:/formulario/fim";
        }
    }


    // Página final
    @GetMapping("/fim")
    public String mostrarFim(@ModelAttribute("respostas") Map<Integer, List<String>> respostas,
                             Model model,
                             HttpSession session) {

        if (session.getAttribute("usuario") == null) {
            return "401";
        }

        model.addAttribute("respostas", respostas);
        return "fim-PerguntasFormulario";
    }
}