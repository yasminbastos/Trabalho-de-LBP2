package com.memorias.diario.controller;

import com.memorias.diario.models.Perfil;
import com.memorias.diario.models.Usuario;
import com.memorias.diario.repositories.PerfilRepository;
import com.memorias.diario.repositories.UsuariosRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Controller
public class PerfilController {

    private static final String UPLOAD_DIR = "./uploads/fotosPerfil/";
    private final UsuariosRepository usuariosRepository;
    private final PerfilRepository perfilRepository;

    public PerfilController(UsuariosRepository usuariosRepository,
                            PerfilRepository perfilRepository) {
        this.usuariosRepository = usuariosRepository;
        this.perfilRepository = perfilRepository;
    }

    private Usuario getUsuarioLogado(HttpSession session) {
        Long id = (Long) session.getAttribute("usuarioId");
        if (id == null) return null;
        return usuariosRepository.findById(id).orElse(null);
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Usuario usuario = getUsuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        Perfil perfil = perfilRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Perfil p = new Perfil();
                    p.setUsuario(usuario);
                    p.setDiasUso(1);
                    return perfilRepository.save(p);
                });

        model.addAttribute("usuario", usuario.getNome());
        model.addAttribute("email", usuario.getEmail());
        model.addAttribute("telefone", perfil.getTelefone());
        model.addAttribute("estadoCivil", perfil.getEstadoCivil());
        model.addAttribute("estado", perfil.getEstado());
        model.addAttribute("cidade", perfil.getCidade());
        model.addAttribute("profissao", perfil.getProfissao());
        model.addAttribute("dataNascimento", perfil.getDataNascimento());
        model.addAttribute("fotoPerfil", perfil.getFotoPerfil());
        model.addAttribute("diasUso", perfil.getDiasUso());

        return "perfil";
    }

    @GetMapping("/editar-perfil")
    public String mostrarEditarPerfil(HttpSession session, Model model) {
        Usuario usuario = getUsuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        Perfil perfil = perfilRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Perfil p = new Perfil();
                    p.setUsuario(usuario);
                    p.setDiasUso(1);
                    return p;
                });

        model.addAttribute("usuario", usuario.getNome());
        model.addAttribute("email", usuario.getEmail());
        model.addAttribute("telefone", perfil.getTelefone());
        model.addAttribute("estadoCivil", perfil.getEstadoCivil());
        model.addAttribute("estado", perfil.getEstado());
        model.addAttribute("cidade", perfil.getCidade());
        model.addAttribute("profissao", perfil.getProfissao());
        model.addAttribute("dataNascimento", perfil.getDataNascimento());
        model.addAttribute("fotoPerfil", perfil.getFotoPerfil());

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
            HttpSession session) {

        Usuario usuario = getUsuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        Perfil perfil = perfilRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Perfil p = new Perfil();
                    p.setUsuario(usuario);
                    p.setDiasUso(1);
                    return p;
                });

        if (nome != null && !nome.trim().isEmpty()) {
            usuario.setNome(nome);
            session.setAttribute("usuario", nome);
        }
        if (email != null && !email.trim().isEmpty()) {
            usuario.setEmail(email);
            session.setAttribute("email", email);
        }
        usuariosRepository.save(usuario);

        perfil.setTelefone(telefone);
        perfil.setEstadoCivil(estadoCivil);
        perfil.setEstado(estado);
        perfil.setCidade(cidade);
        perfil.setProfissao(profissao);
        perfil.setDataNascimento(dataNascimento);

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            try {
                Path pathDiretorio = Paths.get(UPLOAD_DIR);

                Files.createDirectories(pathDiretorio);

                String nomeArquivo = UUID.randomUUID().toString() + "_" + fotoPerfil.getOriginalFilename();
                Path caminhoCompleto = pathDiretorio.resolve(nomeArquivo);

                Files.copy(fotoPerfil.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

                perfil.setFotoPerfil(nomeArquivo);

            } catch (IOException e) {
                System.err.println("Erro ao salvar foto: " + e.getMessage());
            }
        }

        perfilRepository.save(perfil);

        return "redirect:/perfil";
    }
}