package com.memorias.diario.repositories;

import com.memorias.diario.models.RegistroDia;
import com.memorias.diario.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroDiaRepository extends JpaRepository<RegistroDia, Long> {

    List<RegistroDia> findAllByUsuarioOrderByDataDesc(Usuario usuario);

    Optional<RegistroDia> findByUsuarioAndData(Usuario usuario, LocalDate data);
}
