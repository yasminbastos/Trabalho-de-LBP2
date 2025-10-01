package com.memorias.diario.controller;

import com.memorias.diario.models.Usuario;
import com.memorias.diario.repositories.UsuariosRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController {

    private final UsuariosRepository repository;
//    private List<Usuario> usuarios = new ArrayList<>(Arrays.asList(new Usuario("TEste", "teste@teste.com", "teste")));


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

    // Página inicial
    @GetMapping("/")
    public String inicialPage() {
        return "inicial";
    }

    // Página de login
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Login do usuário
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String senha,
                        HttpSession session,
                        Model model) {

        var usuarioExistente = repository.findAllByEmailAndSenha("batata@example.org", "batata");

        if (usuarioExistente.isEmpty()) {
            var novoUsuario = new Usuario();
            novoUsuario.setEmail("batata@example.org");
            novoUsuario.setNome("batata");
            novoUsuario.setSenha("batata");
            repository.save(novoUsuario);
        }

        List<Usuario> resultados = repository.findAllByEmailAndSenha(email, senha);

        if (!resultados.isEmpty()) {
            var usuario = resultados.get(0);  // pega o primeiro usuário

            session.setAttribute("usuario", usuario.getNome());
            session.setAttribute("email", usuario.getEmail());

            return "redirect:/home";
        }

        model.addAttribute("erro", "Email ou senha inválidos");
        return "login";
    }


    // Página de cadastro
    @GetMapping("/cadastro")
    public String cadastroPage() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@RequestParam String nome,
                            @RequestParam String email,
                            @RequestParam String senha,
                            Model model) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha);

        repository.save(novoUsuario);

        model.addAttribute("mensagem", "Cadastro realizado com sucesso!");
        return "login";
    }

    // Página home (apenas para usuários logados)
    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        String nome = (String) session.getAttribute("usuario");
        if (nome == null) { // Se não tiver usuário na sessão
            return "redirect:/login";
        }
        model.addAttribute("nome", nome);
        return "home";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/inicial")
    public String inicial() {
        return "inicial"; // retorna o nome do template inicial.html em templates/
    }

}
