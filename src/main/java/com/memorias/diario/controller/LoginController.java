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

        Usuario u = usuarios.stream()
                .filter(user -> user.getEmail().equals(email) && user.getSenha().equals(senha))
                .findFirst()
                .orElse(null);

        if (u != null) {
            session.setAttribute("usuario", u.getNome());
            return "home";
        } else {
            model.addAttribute("erro", "Email ou senha inválidos");
            return "login";
        }
    }

    @GetMapping("/cadastro")
    public String cadastroPage() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@RequestParam String nome,
                            @RequestParam String email,
                            @RequestParam String senha,
                            Model model) {
        usuarios.add(new Usuario(nome, email, senha));
        model.addAttribute("mensagem", "Cadastro realizado com sucesso!");
        return "login";
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


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
