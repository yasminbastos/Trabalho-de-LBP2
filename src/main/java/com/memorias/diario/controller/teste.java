package com.memorias.diario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class teste {

    @GetMapping("/teste")
    public String teste() {
        return "teste"; // corresponde ao teste.html dentro de templates
    }
}
