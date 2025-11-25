package com.memorias.diario.repositories;

import com.memorias.diario.models.Nota;
import com.memorias.diario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotasRepository extends JpaRepository<Nota, Long> {

    List<Nota> findAllByUsuarioOrderByDataCriacaoDesc(Usuario usuario);

    List<Nota> findAllByUsuarioAndDataCriacaoBetween(
            Usuario usuario,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}
