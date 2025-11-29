package com.memorias.diario.controller;

import com.memorias.diario.models.Usuario;
import com.memorias.diario.repositories.UsuariosRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoginController {

    private final UsuariosRepository repository;

    public LoginController(UsuariosRepository repository){
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        var usuarioExistente = repository.findAllByEmailAndSenha("batata@example.org", "batata");

        if (usuarioExistente.isEmpty()) {
            var novoUsuario = new Usuario();
            novoUsuario.setEmail("batata@example.org");
            novoUsuario.setNome("batata");
            novoUsuario.setSenha("batata");
            repository.save(novoUsuario);
        }
    }

    @GetMapping("/")
    public String inicialPage() {
        return "inicial";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String senha,
                        HttpSession session,
                        Model model) {

        List<Usuario> resultados = repository.findAllByEmailAndSenha(email, senha);

        if (!resultados.isEmpty()) {
            var usuario = resultados.get(0);

            session.setAttribute("usuario", usuario.getNome());
            session.setAttribute("email", usuario.getEmail());
            session.setAttribute("usuarioId", usuario.getId());

            return "redirect:/home";
        }

        model.addAttribute("erro", "Email ou senha inválidos");
        return "login";
    }


    @GetMapping("/cadastro")
    public String cadastroPage() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@RequestParam String nome,
                            @RequestParam String email,
                            @RequestParam String senha,
                            Model model,
                            HttpSession session) {

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha);

        repository.save(novoUsuario);

        session.setAttribute("primeiroAcesso", true);

        session.setAttribute("mensagemCadastro", "Cadastro realizado com sucesso!");

        return "redirect:/formulario";
    }

    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        String nome = (String) session.getAttribute("usuario");
        if (nome == null) {
            return "redirect:/login";
        }
        model.addAttribute("nome", nome);
        return "home";
    }

    @GetMapping("/calendario_anual")
    public String calendarioAnualPage(HttpSession session, Model model) {
        String nome = (String) session.getAttribute("usuario");
        if (nome == null) {
            return "redirect:/login";
        }
        model.addAttribute("nome", nome);
        return "anual";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/inicial")
    public String inicial() {
        return "inicial";
    }

    @GetMapping("/home_manha")
    public String homeManha() {
        return "manha";
    }

    @GetMapping("/home_tarde")
    public String homeTarde() {
        return "tarde";
    }

    @GetMapping("/home_noite")
    public String homeNoite() {
        return "noite";
    }

}
