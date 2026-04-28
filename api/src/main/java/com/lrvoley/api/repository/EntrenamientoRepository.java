package com.lrvoley.api.repository;

import com.lrvoley.api.model.Entrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Integer> {
    List<Entrenamiento> findByIdUsuario(int idUsuario);
    List<Entrenamiento> findByIdUsuarioAndCompletado(int idUsuario, boolean completado);
}
