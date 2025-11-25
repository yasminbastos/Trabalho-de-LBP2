package com.memorias.diario.repositories;

import com.memorias.diario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findAllByEmailAndSenha(String email, String senha);

    Optional<Usuario> findByEmail(String email);
}
