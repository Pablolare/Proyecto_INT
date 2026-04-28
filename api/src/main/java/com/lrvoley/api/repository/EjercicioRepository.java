package com.lrvoley.api.repository;

import com.lrvoley.api.model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {
    List<Ejercicio> findByTipo(String tipo);
}
