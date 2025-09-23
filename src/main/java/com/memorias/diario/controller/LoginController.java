package com.memorias.diario.controller;

import com.memorias.diario.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    private List<Usuario> usuarios = new ArrayList<>();

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

        Usuario u = usuarios.stream()
                .filter(user -> user.getEmail().equals(email) && user.getSenha().equals(senha))
                .findFirst()
                .orElse(null);

        if (u != null) {
            session.setAttribute("usuario", u.getNome());   // Salva o nome na sessão
            session.setAttribute("email", u.getEmail());    // Salva o email na sessão
            return "redirect:/home";                        // Redireciona para home
        } else {
            model.addAttribute("erro", "Email ou senha inválidos");
            return "login";
        }
    }

    // Página de cadastro
    @GetMapping("/cadastro")
    public String cadastroPage() {
        return "cadastro";
    }

    // Cadastrar novo usuário
    @PostMapping("/cadastro")
    public String cadastrar(@RequestParam String nome,
                            @RequestParam String email,
                            @RequestParam String senha,
                            Model model) {
        usuarios.add(new Usuario(nome, email, senha));
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
