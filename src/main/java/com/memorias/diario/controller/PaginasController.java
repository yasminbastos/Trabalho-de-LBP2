package com.memorias.diario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador responsável por exibir as páginas principais do site:
 * Home, Perfil e Formulário.
 * Cada método abaixo cria uma rota acessível no navegador.
 */
@Controller
public class PaginasController {

    /**
     * Rota para a página inicial (Home)
     * URL: http://localhost:8080/home
     */
    @GetMapping("/home")
    public String exibirHome() {
        // Retorna o arquivo "home.html" dentro de src/main/resources/templates/
        return "home";
    }

    /**
     * Rota para a página de Perfil
     * URL: http://localhost:8080/perfil
     */
    @GetMapping("/perfil")
    public String exibirPerfil() {
        // Retorna o arquivo "perfil.html"
        return "perfil";
    }

    /**
     * Rota para a página de Formulário
     * URL: http://localhost:8080/formulario
     */
    @GetMapping("/formulario")
    public String exibirFormulario() {
        // Retorna o arquivo "formulario.html"
        return "formulario";
    }

    @GetMapping("/cadastro")
    public String exibirCadastro() {
    // Retorna o arquivo "cadastro.html"
    return "cadastro";
    }

    @GetMapping("/login")
    public String exibirLogin() {
        // Retorna o arquivo "login.html"
        return "login";
    }

    @GetMapping("/inicial")
    public String exibirInicial() {
        // Retorna o arquivo "inicial.html"
        return "inicial";
    }
}
