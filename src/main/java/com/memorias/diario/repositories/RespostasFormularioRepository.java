package com.memorias.diario.repositories;

import com.memorias.diario.models.RespostaFormulario;
import com.memorias.diario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespostasFormularioRepository extends JpaRepository<RespostaFormulario, Long> {
    List<RespostaFormulario> findAllByUsuario(Usuario usuario);
}
