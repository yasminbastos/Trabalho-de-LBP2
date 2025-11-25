package com.memorias.diario.repositories;

import com.memorias.diario.models.Perfil;
import com.memorias.diario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByUsuario(Usuario usuario);
}
