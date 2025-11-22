package com.memorias.diario.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Controller
public class PerfilController {

    // Pasta onde as fotos serão salvas
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        String email = (String) session.getAttribute("email");

        if (usuario == null || email == null) {
            return "redirect:/login";
        }

        // Carregar dados do perfil da sessão
        model.addAttribute("usuario", usuario);
        model.addAttribute("email", email);
        model.addAttribute("telefone", session.getAttribute("telefone"));
        model.addAttribute("estadoCivil", session.getAttribute("estadoCivil"));
        model.addAttribute("estado", session.getAttribute("estado"));
        model.addAttribute("cidade", session.getAttribute("cidade"));
        model.addAttribute("profissao", session.getAttribute("profissao"));
        model.addAttribute("dataNascimento", session.getAttribute("dataNascimento"));
        model.addAttribute("fotoPerfil", session.getAttribute("fotoPerfil"));

        // Dias de uso (exemplo)
        model.addAttribute("diasUso", session.getAttribute("diasUso") != null ?
                session.getAttribute("diasUso") : 1);

        return "perfil";
    }

    @GetMapping("/editar-perfil")
    public String mostrarEditarPerfil(HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        String email = (String) session.getAttribute("email");

        if (usuario == null || email == null) {
            return "redirect:/login";
        }

        // Carregar dados existentes para o formulário de edição
        model.addAttribute("usuario", usuario);
        model.addAttribute("email", email);
        model.addAttribute("telefone", session.getAttribute("telefone"));
        model.addAttribute("estadoCivil", session.getAttribute("estadoCivil"));
        model.addAttribute("estado", session.getAttribute("estado"));
        model.addAttribute("cidade", session.getAttribute("cidade"));
        model.addAttribute("profissao", session.getAttribute("profissao"));
        model.addAttribute("dataNascimento", session.getAttribute("dataNascimento"));
        model.addAttribute("fotoPerfil", session.getAttribute("fotoPerfil"));

        return "editar-perfil";
    }

    @PostMapping("/atualizar-perfil")
    public String atualizarPerfil(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "telefone", required = false) String telefone,
            @RequestParam(value = "estadoCivil", required = false) String estadoCivil,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "cidade", required = false) String cidade,
            @RequestParam(value = "profissao", required = false) String profissao,
            @RequestParam(value = "dataNascimento", required = false) String dataNascimento,
            @RequestParam(value = "fotoPerfil", required = false) MultipartFile fotoPerfil,
            HttpSession session) throws IOException {

        // Atualizar dados na sessão
        if (nome != null && !nome.trim().isEmpty()) {
            session.setAttribute("usuario", nome);
        }
        if (email != null && !email.trim().isEmpty()) {
            session.setAttribute("email", email);
        }
        session.setAttribute("telefone", telefone);
        session.setAttribute("estadoCivil", estadoCivil);
        session.setAttribute("estado", estado);
        session.setAttribute("cidade", cidade);
        session.setAttribute("profissao", profissao);
        session.setAttribute("dataNascimento", dataNascimento);

        // Processar upload da foto de perfil
        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            try {
                // Criar diretório se não existir
                Files.createDirectories(Paths.get(UPLOAD_DIR));

                // Gerar nome único para o arquivo
                String nomeArquivo = UUID.randomUUID().toString() +
                        "_" + fotoPerfil.getOriginalFilename();

                // Salvar arquivo
                Path caminho = Paths.get(UPLOAD_DIR + nomeArquivo);
                Files.write(caminho, fotoPerfil.getBytes());

                // Salvar caminho relativo na sessão
                session.setAttribute("fotoPerfil", "/uploads/" + nomeArquivo);

            } catch (IOException e) {
                System.err.println("Erro ao salvar foto: " + e.getMessage());
            }
        }

        return "redirect:/perfil";
    }

    @PostMapping("/upload-foto")
    @ResponseBody
    public Map<String, String> uploadFoto(@RequestParam("foto") MultipartFile foto,
                                          HttpSession session) {
        Map<String, String> response = new HashMap<>();

        try {
            if (foto.isEmpty()) {
                response.put("status", "error");
                response.put("message", "Arquivo vazio");
                return response;
            }

            // Criar diretório se não existir
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // Gerar nome único
            String nomeArquivo = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();

            // Salvar arquivo
            Path caminho = Paths.get(UPLOAD_DIR + nomeArquivo);
            Files.write(caminho, foto.getBytes());

            // Atualizar sessão
            String caminhoRelativo = "/uploads/" + nomeArquivo;
            session.setAttribute("fotoPerfil", caminhoRelativo);

            response.put("status", "success");
            response.put("caminho", caminhoRelativo);

        } catch (IOException e) {
            response.put("status", "error");
            response.put("message", "Erro ao salvar arquivo: " + e.getMessage());
        }

        return response;
    }
}