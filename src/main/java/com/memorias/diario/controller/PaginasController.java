package com.memorias.diario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PaginasController {

    // URL: http://localhost:8080/formulario
    @GetMapping("/formulario")
    public String exibirFormulario() {
        return "formulario";
    }
}