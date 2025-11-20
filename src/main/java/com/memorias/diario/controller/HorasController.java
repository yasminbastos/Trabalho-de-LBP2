package com.memorias.diario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HorasController {

    @GetMapping("/manha")
    public String manha() {
        return "manha";
    }

    @GetMapping("/tarde")
    public String tarde() {
        return "tarde";
    }

    @GetMapping("/noite")
    public String noite() {
        return "noite";
    }
}