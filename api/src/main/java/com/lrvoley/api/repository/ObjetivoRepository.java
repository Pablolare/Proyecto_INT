package com.lrvoley.api.repository;

import com.lrvoley.api.model.Objetivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObjetivoRepository extends JpaRepository<Objetivo, Integer> {
    List<Objetivo> findByIdUsuario(int idUsuario);
    List<Objetivo> findByIdUsuarioAndCumplido(int idUsuario, boolean cumplido);
}
